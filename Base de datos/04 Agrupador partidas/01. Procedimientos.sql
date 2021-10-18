if object_id('dbo.sp_obtPartidaXproceso') is not null
begin
	drop procedure dbo.sp_obtPartidaXproceso
end
go

if object_id('dbo.sp_InsPartidaDetalleFicha') is not null
begin
	drop procedure dbo.sp_InsPartidaDetalleFicha
end
go

if object_id('dbo.sp_valCrearRecorte') is not null
begin
	drop procedure dbo.sp_valCrearRecorte
end
go

if object_id('dbo.sp_insRecorte') is not null
begin
	drop procedure dbo.sp_insRecorte
end
go

if object_id('dbo.sp_actBajasPartidaDet') is not null
begin
	drop procedure dbo.sp_actBajasPartidaDet
end
go

if object_id('dbo.sp_obtCueroEngraseDisp') is not null
begin
	drop procedure dbo.sp_obtCueroEngraseDisp
end
go

if object_id('dbo.sp_insInvCross') is not null
begin
	drop procedure dbo.sp_insInvCross
end
go

if object_id('dbo.sp_obtEntCross') is not null
begin
	drop procedure dbo.sp_obtEntCross
end
go

if object_id('dbo.sp_agrInvCrossSemi') is not null
begin
	drop procedure dbo.sp_agrInvCrossSemi
end
go

if object_id('dbo.sp_agrBajaInvCross') is not null
begin
	drop procedure dbo.sp_agrBajaInvCross
end
go

if object_id('dbo.sp_obtInvCrossSemi') is not null
begin
	drop procedure dbo.sp_obtInvCrossSemi
end
go

if object_id('dbo.sp_agrInvSemi') is not null
begin
	drop procedure dbo.sp_agrInvSemi
end
go

if object_id('dbo.sp_obtEntInvSem') is not null
begin
	drop procedure dbo.sp_obtEntInvSem
end
go

if object_id('dbo.sp_agrInvSemTer') is not null
begin
	drop procedure dbo.sp_agrInvSemTer
end
go

if object_id('dbo.sp_agrBajaInvSemiterminado') is not null
begin
	drop procedure dbo.sp_agrBajaInvSemiterminado
end
go

if object_id('dbo.sp_obtInvSemTer') is not null
begin
	drop procedure dbo.sp_obtInvSemTer
end
go

if object_id('dbo.Usp_InvSemTerGetAgrupado') is not null
begin
	drop procedure dbo.Usp_InvSemTerGetAgrupado
end
go

if object_id('dbo.Usp_InvSemiterminadoregresar') is not null
begin
	drop procedure dbo.Usp_InvSemiterminadoregresar
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_obtPartidaXproceso
(
	@idProceso int
)
as begin
  
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   DLuna     ????/??/??   Creación
    02   DLuna     2021/05/17   Se agrupan recortes por partida
  =================================================================================================================================
  */ 
  
	if @idProceso in (2,3,4)
  begin
    select
      pa.NoPartida
      , case
          when tr.descripcion = 'Entero' then 'Entero'
          when tr.descripcion = 'Delantero Sillero' then 'Delantero'
          when tr.descripcion = 'Crupon Sillero' then 'Crupon'
          when tr.descripcion = 'Lados' then 'Lados'
          when tr.descripcion = 'Centro CastaÃ±o' then 'Centro CastaÃ±o'
          when tr.descripcion = 'Centro Quebracho' then 'Centro Quebracho'
          when tr.descripcion = 'Centro' then 'Centro'
          when tr.descripcion = 'Delantero Suela' then 'Delantero Suela'
          else tr.descripcion
        end as descripcion
      , [noPiezasAct] = sum(pd.noPiezasAct)
      , pa.idPartida
      , tr.idTipoRecorte
      
    from
      tb_partidaDet as pd
      
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = pd.idTipoRecorte
        
      inner join
        tb_partida as pa
      on
        pa.idPartida = pd.idPartida
      
    where
      pd.noPiezasAct > 0
      and pd.idProceso = @idProceso - 1
    
    group by
      pa.NoPartida
      , tr.descripcion
      , pa.idPartida
      , tr.idTipoRecorte
  end
  
  else
  begin
    select
      pa.NoPartida
      , tr.descripcion
      , [noPiezasAct] = sum(pd.noPiezasAct)
      , pa.idPartida
      , tr.idTipoRecorte
      
    from
      tb_partidaDet as pd
      
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = pd.idTipoRecorte
        
      inner join
        tb_partida as pa
      on
        pa.idPartida = pd.idPartida
      
    where
      pd.noPiezasAct > 0
      and pd.idProceso = @idProceso - 1
    
    group by
      pa.NoPartida
      , tr.descripcion
      , pa.idPartida
      , tr.idTipoRecorte
  end
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_InsPartidaDetalleFicha
(
  @noPiezas            int
  , @idPartida         int
  , @idTipoRecorte     int
  , @idProceso         int
  , @idFichaProd       int 
  , @noPiezasTotal     int
  , @kgPartida         float
  , @kgTotal           float
  , @costoInsumosFicha float
)
as begin

  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   DLuna     ????/??/??   Creación
    02   DLuna     2021/05/17   Se borra parámetro @idPartidaDet para realizar descuentos de piezas cálculadas y se ejecuta
                                sp_InsFichaProdDet para generar detalle de las fichas de producción por cada PartidaDet
  =================================================================================================================================
  */ 
  
  declare 
    @idPartidaDet        int
    , @noPiezasAct       int
    , @idRecepcionCuero  int
    , @idInventarioCrudo int
    , @kgPartidaXkg      float
  
  drop table if exists #TmpPartidaDet
  
  select
    @kgPartidaXkg = @kgPartida / @noPiezas
  
  /* ------------------- */
  select
    idPartidaDet
    , noPiezasAct
    , idRecepcionCuero
    , idProceso
    , idInventarioCrudo
  into
    #TmpPartidaDet
  from
    tb_partidaDet
  where
    idPartida = @idPartida
    and idTipoRecorte = @idTipoRecorte
    and idProceso = @idProceso - 1
    and noPiezasAct > 0
  /* ------------------- */
  
  begin try
  
    while ( ((select count(1) from #TmpPartidaDet) > 0) and @noPiezas > 0)
    begin
      
      declare
        @noPiezasUpd int = 0
        , @idPartidaDetNueva int
        , @kgPartidaDet float
      
      select top 1
        @idPartidaDet = idPartidaDet
        , @noPiezasAct = noPiezasAct
        , @idRecepcionCuero = idRecepcionCuero
        , @idProceso = idProceso
        , @idInventarioCrudo = idInventarioCrudo
      from
        #TmpPartidaDet
      --
      
      if (@noPiezasAct < @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = @noPiezas - @noPiezasAct
      end
      else if (@noPiezasAct > @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezas
        set @noPiezas = 0
      end
      else
      begin
      
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = 0
      end
      --
      
      set
        @kgPartidaDet = @kgPartidaXkg * @noPiezasUpd
      --
      
      insert into
        tb_partidaDet
        (
          noPiezas
          , noPiezasAct
          , idPartida
          , idRecepcionCuero
          , idTipoRecorte
          , idProceso
          , idInventarioCrudo
          , procedenciaCrudo
          , idRecortePartidaDet
          
        )
      values
        (
          @noPiezasUpd
          , @noPiezasUpd
          , @idPartida
          , @idRecepcionCuero
          , @idTipoRecorte
          , @idProceso + 1
          , @idInventarioCrudo
          , 0
          , @idPartidaDet
        )
      
      --
      update
        tb_partidaDet
      set
        noPiezasAct = noPiezasAct - @noPiezasUpd
      where
        idPartidaDet = @idPartidaDet
      
      --
      delete from 
        #TmpPartidaDet
      where
        idPartidaDet = @idPartidaDet
        
      --      
      select
        @idPartidaDetNueva = max(idPartidaDet)
      from
        tb_partidaDet
      
      --
      exec sp_InsFichaProdDet
        @idFichaProd          = @idFichaProd 
        , @idPartidaDet       = @idPartidaDetNueva
        , @noPiezasPartida    = @noPiezasUpd
        , @noPiezasTotal      = @noPiezasTotal
        , @kgPartida          = @kgPartidaDet
        , @kgTotal            = @kgTotal
        , @costoInsumosFicha  = @costoInsumosFicha
      
    end
    
  end try
  begin catch
    
    print 'Error al ejecutar '+ 'sp_obtPartidaXproceso ' + ERROR_MESSAGE()
  end catch
  
end
GO

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_valCrearRecorte
(
	@idPartida             int
  , @idRecepcionCuero    int = 0 --#02
  , @idTipoRecorte       int
  , @idProceso           int
)
as begin
  
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   DLuna     ????/??/??   Creación
    02   DLuna     2021/06/02   Se hace opcional el envío del parámetro @idRecepcionCuero
  =================================================================================================================================
  */ 
  
  if (@idProceso = 2)
  begin
    
    if (@idTipoRecorte = 0)
    begin
      
      set @idTipoRecorte = 2
    end
    
    else if (@idTipoRecorte = 1)
    begin
      
      set @idTipoRecorte = 5
    end
    
    else if (@idTipoRecorte = 2)
    begin
      
      set @idTipoRecorte = 6
    end
    
    
    if exists
    (
      select
        1
      from
        tb_partidaDet
      where
        idPartida = @idPartida
        and idRecepcionCuero = @idRecepcionCuero
        and idTipoRecorte = @idTipoRecorte
        and idProceso = 1
    )
    begin
      
      select
        [bandera] = 0
    end
    
    else begin
    
      select
        [bandera] = 1
    end
  
  end
  
  else begin
    
      select
        [bandera] = 1
  end
  
end
GO

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_insRecorte
(
  @idTipoRecorte         int
  , @noPiezasArecortar   int
  , @noPiezasNuevas      int
  , @idPartida           int
  , @idProceso           int
  , @idTipoRecorteOrigen int --#02
)
as begin

  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   DLuna     ????/??/??   Creación
    02   DLuna     2021/06/02   Se agrega cálculo autómatico de piezas a recortar por agrupador de partidas
  =================================================================================================================================
  */ 
  
  declare 
    @idRecepcionCuero     int
    , @idInventarioCrudo  int
    , @garra              float
    , @garraDesc          float
    , @noGarras           int
    , @ultimaIdPartidaDet int
    , @kgTotal            float
    , @costoTotal         float
    , @costoInsumos       float
    , @porcCuero          float
    --, @noPiezasArecortarAux       int  
    
  drop table if exists #TmpPartidaDetRecorte
  
  /*
  select
    @noPiezasArecortarAux = @noPiezasNuevas / @noPiezasArecortar
  */
  
  /* ------------------- */
  select
    idPartidaDet
    , noPiezasAct
  into
    #TmpPartidaDetRecorte
  from
    tb_partidaDet
  where
    idPartida = @idPartida
    and idTipoRecorte = @idTipoRecorteOrigen
    and idProceso = @idProceso
  /* ------------------- */
  
  begin try
  
    while ( ((select count(1) from #TmpPartidaDetRecorte) > 0) and @noPiezasArecortar > 0)
    begin
      
      declare
        @idPartidaDet int
        , @noPiezasAct int
        , @noPiezasArecortarAux int
      
      --
      select top 1
        @idPartidaDet = idPartidaDet
        , @noPiezasAct = noPiezasAct
      from
        #TmpPartidaDetRecorte

      --
      if (@noPiezasAct < @noPiezasArecortar)
      begin
        
        set @noPiezasArecortarAux = @noPiezasAct
        set @noPiezasArecortar = @noPiezasArecortar - @noPiezasAct
      end
      else if (@noPiezasAct > @noPiezasArecortar)
      begin
        
        set @noPiezasArecortarAux = @noPiezasArecortar
        set @noPiezasArecortar = 0
      end
      else
      begin
      
        set @noPiezasArecortarAux = @noPiezasAct
        set @noPiezasArecortar = 0
      end
        
      --  
      update
        tb_partidaDet
      set
        noPiezasAct = noPiezasAct - @noPiezasArecortarAux
      where
        idPartidaDet = @idPartidaDet
      
      --
      select
        @idRecepcionCuero = idRecepcionCuero
        , @idInventarioCrudo = idInventarioCrudo
      from
        tb_partidaDet
      where
        idPartidaDet = @idPartidaDet
          
        
      if @idTipoRecorte = 0
      begin
      
        insert into
          tb_partidaDet
          (
            noPiezas
            , noPiezasAct
            , idPartida
            , idRecepcionCuero
            , idTipoRecorte
            , idProceso
            , idInventarioCrudo
            , procedenciaCrudo
            , idRecortePartidaDet
          )
          
        values
          (
            @noPiezasArecortarAux
            , @noPiezasArecortarAux
            , @idPartida
            , @idRecepcionCuero
            , 2
            , @idProceso
            , @idInventarioCrudo
            , 0
            , @idPartidaDet
          )
        
        select
          @porcCuero = porcentaje
        from
          tb_confPrecioCuero
        where
          idTipoRecorte = 2
          
        select
          @kgTotal = ((kgTotal / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoTotal = ((costoTotalCuero / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoInsumos = ((costoInsumos / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
        from
          tb_fichaProdDet
        where
          idPartidaDet = @idPartidaDet
          
        if (@kgTotal is null or @costoTotal is null or @costoInsumos is null)
        begin
          select
            @kgTotal = ((pda.kgTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoTotal = ((pda.costoTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoInsumos = ((pda.costoInsumos / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
          from
            tb_partidaDetAux as pda
          inner join
            tb_partidaDet as pd
          on
            pd.idPartidaDet = pda.idPartidaDet
          where
            pda.idPartidaDet = @idPartidaDet
          end
          
          set
            @ultimaIdPartidaDet =
            (
              select
                max(idPartidaDet)
              from
                tb_partidaDet
            )
        
          insert into
            tb_partidaDetAux
            (
              idPartidaDet
              , kgTotal
              , costoTotal
              , costoInsumos
            )
          
          values
            (
              @ultimaIdPartidaDet
              , @kgTotal
              , @costoTotal
              , @costoInsumos
            )
          
          insert into
            tb_partidaDet
            (
              noPiezas
              , noPiezasAct
              , idPartida
              , idRecepcionCuero
              , idTipoRecorte
              , idProceso
              , idInventarioCrudo
              , procedenciaCrudo
              , idRecortePartidaDet
            )
            
          values
            (
              @noPiezasArecortarAux
              , @noPiezasArecortarAux
              , @idPartida
              , @idRecepcionCuero
              , 3
              , @idProceso
              , @idInventarioCrudo
              , 0
              , @idPartidaDet
            )
          
          select
            @porcCuero = porcentaje
          from
            tb_confPrecioCuero
          where
            idTipoRecorte = 3
          
          select
            @kgTotal = ((kgTotal / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
            , @costoTotal = ((costoTotalCuero / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
            , @costoInsumos = ((costoInsumos / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          from
            tb_fichaProdDet
          where
            idPartidaDet = @idPartidaDet
          
          if (@kgTotal is null or @costoTotal is null or @costoInsumos is null)
          begin
            select
              @kgTotal = ((pda.kgTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
              , @costoTotal = ((pda.costoTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
              , @costoInsumos = ((pda.costoInsumos / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            from
              tb_partidaDetAux as pda
            inner join
              tb_partidaDet as pd
            on
              pd.idPartidaDet = pda.idPartidaDet
            where
              pda.idPartidaDet = @idPartidaDet
          end
          
          set
            @ultimaIdPartidaDet =
            (
              select
                max(idPartidaDet)
              from
                tb_partidaDet
            )
          
          insert into
            tb_partidaDetAux
            (
              idPartidaDet
              , kgTotal
              , costoTotal
              , costoInsumos
            )
          
          values
            (
              @ultimaIdPartidaDet
              , @kgTotal
              , @costoTotal
              , @costoInsumos
            )
          
          set
            @garra =
            (
              select
                costo
                
              from
                tb_costoGarra
                
              where
                idCostoGarra =
                (
                  select
                    max(idCostoGarra)
                    
                  from
                    tb_costoGarra
                )
            )
            
          set
            @noGarras = @noPiezasArecortarAux*2
            
          set
            @garraDesc = @garra * @noGarras
          
          insert into
            tb_garrasPartida
            (
              idPartida
              , noGarras
              , costoTotalGarras
            )
            
          values
            (
              @idPartida
              , @noGarras
              , @garraDesc
            )
        end
      
      else if @idTipoRecorte = 1
      begin
      
        insert into
          tb_partidaDet
          (
            noPiezas
            , noPiezasAct
            , idPartida
            , idRecepcionCuero
            , idTipoRecorte
            , idProceso
            , idInventarioCrudo
            , procedenciaCrudo
            , idRecortePartidaDet
          )
          
        values
          (
            @noPiezasArecortarAux
            , @noPiezasArecortarAux
            , @idPartida
            , @idRecepcionCuero
            , 5
            , @idProceso
            , @idInventarioCrudo
            , 0
            , @idPartidaDet
          )
          
        select
          @porcCuero = porcentaje
        from
          tb_confPrecioCuero
        where
          idTipoRecorte = 5
          
        select
          @kgTotal = ((kgTotal / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoTotal = ((costoTotalCuero / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoInsumos = ((costoInsumos / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
        from
          tb_fichaProdDet
        where
          idPartidaDet = @idPartidaDet
          
        if (@kgTotal is null or @costoTotal is null or @costoInsumos is null)
        begin
          select
            @kgTotal = ((pda.kgTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoTotal = ((pda.costoTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoInsumos = ((pda.costoInsumos / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
          from
            tb_partidaDetAux as pda
          inner join
            tb_partidaDet as pd
          on
            pd.idPartidaDet = pda.idPartidaDet
          where
            pda.idPartidaDet = @idPartidaDet
        end
          
        set
          @ultimaIdPartidaDet =
          (
            select
              max(idPartidaDet)
            from
              tb_partidaDet
          )
        
        insert into
          tb_partidaDetAux
          (
            idPartidaDet
            , kgTotal
            , costoTotal
            , costoInsumos
          )
        
        values
          (
            @ultimaIdPartidaDet
            , @kgTotal
            , @costoTotal
            , @costoInsumos
          )
          
          insert into
            tb_partidaDet
            (
              noPiezas
              , noPiezasAct
              , idPartida
              , idRecepcionCuero
              , idTipoRecorte
              , idProceso
              , idInventarioCrudo
              , procedenciaCrudo
              , idRecortePartidaDet
            )
          
        values
          (
            @noPiezasArecortarAux
            , @noPiezasArecortarAux
            , @idPartida
            , @idRecepcionCuero
            , 7
            , @idProceso
            , @idInventarioCrudo
            , 0
            , @idPartidaDet
          )
          
        select
          @porcCuero = porcentaje
        from
          tb_confPrecioCuero
        where
          idTipoRecorte = 7
          
        select
          @kgTotal = ((kgTotal / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoTotal = ((costoTotalCuero / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoInsumos = ((costoInsumos / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
        from
          tb_fichaProdDet
        where
          idPartidaDet = @idPartidaDet
          
        if (@kgTotal is null or @costoTotal is null or @costoInsumos is null)
        begin
          select
            @kgTotal = ((pda.kgTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoTotal = ((pda.costoTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoInsumos = ((pda.costoInsumos / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
          from
            tb_partidaDetAux as pda
          inner join
            tb_partidaDet as pd
          on
            pd.idPartidaDet = pda.idPartidaDet
          where
            pda.idPartidaDet = @idPartidaDet
        end
          
        set
          @ultimaIdPartidaDet =
          (
            select
              max(idPartidaDet)
            from
              tb_partidaDet
          )
        
        insert into
          tb_partidaDetAux
          (
            idPartidaDet
            , kgTotal
            , costoTotal
            , costoInsumos
          )
        
        values
          (
            @ultimaIdPartidaDet
            , @kgTotal
            , @costoTotal
            , @costoInsumos
          )
          
        set
          @garra =
          (
            select
              costo
              
            from
              tb_costoGarra
              
            where
              idCostoGarra =
              (
                select
                  max(idCostoGarra)
                  
                from
                  tb_costoGarra
              )
          )
        
        set
          @noGarras = @noPiezasArecortarAux
          
        set
          @garraDesc = @garra * @noGarras
        
        insert into
          tb_garrasPartida
          (
            idPartida
            , noGarras
            , costoTotalGarras
          )
          
        values
          (
            @idPartida
            , @noGarras
            , @garraDesc
          )
      end
      
      else if @idTipoRecorte = 2
      begin
      
        insert into
          tb_partidaDet
          (
            noPiezas
            , noPiezasAct
            , idPartida
            , idRecepcionCuero
            , idTipoRecorte
            , idProceso
            , idInventarioCrudo
            , procedenciaCrudo
            , idRecortePartidaDet
          )
          
        values
          (
            @noPiezasArecortarAux
            , @noPiezasArecortarAux
            , @idPartida
            , @idRecepcionCuero
            , 6
            , @idProceso
            , @idInventarioCrudo
            , 0
            , @idPartidaDet
          )
          
        select
          @porcCuero = porcentaje
        from
          tb_confPrecioCuero
        where
          idTipoRecorte = 6
          
        select
          @kgTotal = ((kgTotal / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoTotal = ((costoTotalCuero / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoInsumos = ((costoInsumos / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
        from
          tb_fichaProdDet
        where
          idPartidaDet = @idPartidaDet
          
        if (@kgTotal is null or @costoTotal is null or @costoInsumos is null)
        begin
          select
            @kgTotal = ((pda.kgTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoTotal = ((pda.costoTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoInsumos = ((pda.costoInsumos / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
          from
            tb_partidaDetAux as pda
          inner join
            tb_partidaDet as pd
          on
            pd.idPartidaDet = pda.idPartidaDet
          where
            pda.idPartidaDet = @idPartidaDet
        end
          
        set
          @ultimaIdPartidaDet =
          (
            select
              max(idPartidaDet)
            from
              tb_partidaDet
          )
        
        insert into
          tb_partidaDetAux
          (
            idPartidaDet
            , kgTotal
            , costoTotal
            , costoInsumos
          )
        
        values
          (
            @ultimaIdPartidaDet
            , @kgTotal
            , @costoTotal
            , @costoInsumos
          )
          
        insert into
          tb_partidaDet
          (
            noPiezas
            , noPiezasAct
            , idPartida
            , idRecepcionCuero
            , idTipoRecorte
            , idProceso
            , idInventarioCrudo
            , procedenciaCrudo
            , idRecortePartidaDet
          )
          
        values
          (
            @noPiezasArecortarAux
            , @noPiezasArecortarAux
            , @idPartida
            , @idRecepcionCuero
            , 7
            , @idProceso
            , @idInventarioCrudo
            , 0
            , @idPartidaDet
          )
          
        select
          @porcCuero = porcentaje
        from
          tb_confPrecioCuero
        where
          idTipoRecorte = 7
          
        select
          @kgTotal = ((kgTotal / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoTotal = ((costoTotalCuero / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoInsumos = ((costoInsumos / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
        from
          tb_fichaProdDet
        where
          idPartidaDet = @idPartidaDet
          
        if (@kgTotal is null or @costoTotal is null or @costoInsumos is null)
        begin
          select
            @kgTotal = ((pda.kgTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoTotal = ((pda.costoTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoInsumos = ((pda.costoInsumos / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
          from
            tb_partidaDetAux as pda
          inner join
            tb_partidaDet as pd
          on
            pd.idPartidaDet = pda.idPartidaDet
          where
            pda.idPartidaDet = @idPartidaDet
        end
          
        set
          @ultimaIdPartidaDet =
          (
            select
              max(idPartidaDet)
            from
              tb_partidaDet
          )
        
        insert into
          tb_partidaDetAux
          (
            idPartidaDet
            , kgTotal
            , costoTotal
            , costoInsumos
          )
        
        values
          (
            @ultimaIdPartidaDet
            , @kgTotal
            , @costoTotal
            , @costoInsumos
          )
          
          set
          @garra =
          (
            select
              costo
              
            from
              tb_costoGarra
              
            where
              idCostoGarra =
              (
                select
                  max(idCostoGarra)
                  
                from
                  tb_costoGarra
              )
          )
        
        set
          @noGarras = @noPiezasArecortarAux
          
        set
          @garraDesc = @garra * @noGarras
        
        insert into
          tb_garrasPartida
          (
            idPartida
            , noGarras
            , costoTotalGarras
          )

        values
          (
            @idPartida
            , @noGarras
            , @garraDesc
          )
      end
      
      else if @idTipoRecorte = 3
      begin
      
        insert into
          tb_partidaDet
          (
            noPiezas
            , noPiezasAct
            , idPartida
            , idRecepcionCuero
            , idTipoRecorte
            , idProceso
            , idInventarioCrudo
            , procedenciaCrudo
            , idRecortePartidaDet
          )
          
        values
          (
            @noPiezasArecortarAux
            , @noPiezasArecortarAux
            , @idPartida
            , @idRecepcionCuero
            , 8
            , @idProceso
            , @idInventarioCrudo
            , 0
            , @idPartidaDet
          )
        
        select
          @porcCuero = porcentaje
        from
          tb_confPrecioCuero
        where
          idTipoRecorte = 8
          
        select
          @kgTotal = ((kgTotal / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoTotal = ((costoTotalCuero / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoInsumos = ((costoInsumos / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
        from
          tb_fichaProdDet
        where
          idPartidaDet = @idPartidaDet
          
        if (@kgTotal is null or @costoTotal is null or @costoInsumos is null)
        begin
          select
            @kgTotal = ((pda.kgTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoTotal = ((pda.costoTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoInsumos = ((pda.costoInsumos / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
          from
            tb_partidaDetAux as pda
          inner join
            tb_partidaDet as pd
          on
            pd.idPartidaDet = pda.idPartidaDet
          where
            pda.idPartidaDet = @idPartidaDet
        end
          
        set
          @ultimaIdPartidaDet =
          (
            select
              max(idPartidaDet)
            from
              tb_partidaDet
          )
        
        insert into
          tb_partidaDetAux
          (
            idPartidaDet
            , kgTotal
            , costoTotal
            , costoInsumos
          )
        
        values
          (
            @ultimaIdPartidaDet
            , @kgTotal
            , @costoTotal
            , @costoInsumos
          )
          
        insert into
          tb_partidaDet
          (
            noPiezas
            , noPiezasAct
            , idPartida
            , idRecepcionCuero
            , idTipoRecorte
            , idProceso
            , idInventarioCrudo
            , procedenciaCrudo
            , idRecortePartidaDet
          )
          
        values
          (
            @noPiezasArecortarAux
            , @noPiezasArecortarAux
            , @idPartida
            , @idRecepcionCuero
            , 7
            , @idProceso
            , @idInventarioCrudo
            , 0
            , @idPartidaDet
          )
        
        select
          @porcCuero = porcentaje
        from
          tb_confPrecioCuero
        where
          idTipoRecorte = 7
          
        select
          @kgTotal = ((kgTotal / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoTotal = ((costoTotalCuero / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoInsumos = ((costoInsumos / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
        from
          tb_fichaProdDet
        where
          idPartidaDet = @idPartidaDet
          
        if (@kgTotal is null or @costoTotal is null or @costoInsumos is null)
        begin
          select
            @kgTotal = ((pda.kgTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoTotal = ((pda.costoTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoInsumos = ((pda.costoInsumos / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
          from
            tb_partidaDetAux as pda
          inner join
            tb_partidaDet as pd
          on
            pd.idPartidaDet = pda.idPartidaDet
          where
            pda.idPartidaDet = @idPartidaDet
        end
          
        set
          @ultimaIdPartidaDet =
          (
            select
              max(idPartidaDet)
            from
              tb_partidaDet
          )
        
        insert into
          tb_partidaDetAux
          (
            idPartidaDet
            , kgTotal
            , costoTotal
            , costoInsumos
          )
        
        values
          (
            @ultimaIdPartidaDet
            , @kgTotal
            , @costoTotal
            , @costoInsumos
          )
          
          set
          @garra =
          (
            select
              costo
              
            from
              tb_costoGarra
              
            where
              idCostoGarra =
              (
                select
                  max(idCostoGarra)
                  
                from
                  tb_costoGarra
              )
          )
        
        set
          @noGarras = @noPiezasArecortarAux
          
        set
          @garraDesc = @garra * @noGarras
        
        insert into
          tb_garrasPartida
          (
            idPartida
            , noGarras
            , costoTotalGarras
          )
          
        values
          (
            @idPartida
            , @noGarras
            , @garraDesc
          )
      end
      
      else begin
        
        insert into
          tb_partidaDet
          (
            noPiezas
            , noPiezasAct
            , idPartida
            , idRecepcionCuero
            , idTipoRecorte
            , idProceso
            , idInventarioCrudo
            , procedenciaCrudo
            , idRecortePartidaDet
          )
        
        values
          (
            @noPiezasArecortarAux
            , @noPiezasArecortarAux
            , @idPartida
            , @idRecepcionCuero
            , @idTipoRecorte
            , @idProceso 
            , @idInventarioCrudo
            , 0
            , @idPartidaDet
          )
          
        select
          @porcCuero = porcentaje
        from
          tb_confPrecioCuero
        where
          idTipoRecorte = @idTipoRecorte
          
        select
          @kgTotal = ((kgTotal / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoTotal = ((costoTotalCuero / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
          , @costoInsumos = ((costoInsumos / noPiezasTotal) * @noPiezasArecortarAux) * @porcCuero
        from
          tb_fichaProdDet
        where
          idPartidaDet = @idPartidaDet
          
        if (@kgTotal is null or @costoTotal is null or @costoInsumos is null)
        begin
          select
            @kgTotal = ((pda.kgTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoTotal = ((pda.costoTotal / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
            , @costoInsumos = ((pda.costoInsumos / pd.noPiezas) * @noPiezasArecortarAux) * @porcCuero
          from
            tb_partidaDetAux as pda
          inner join
            tb_partidaDet as pd
          on
            pd.idPartidaDet = pda.idPartidaDet
          where
            pda.idPartidaDet = @idPartidaDet
        end
          
        set
          @ultimaIdPartidaDet =
          (
            select
              max(idPartidaDet)
            from
              tb_partidaDet
          )
        
        insert into
          tb_partidaDetAux
          (
            idPartidaDet
            , kgTotal
            , costoTotal
            , costoInsumos
          )
        
        values
          (
            @ultimaIdPartidaDet
            , @kgTotal
            , @costoTotal
            , @costoInsumos
          )
      end
      
      --
      delete from 
        #TmpPartidaDetRecorte
      where
        idPartidaDet = @idPartidaDet
        
    end -- while
  
  end try
  begin catch
    print 'Error al ejecutar '+ 'sp_insRecorte ' + ERROR_MESSAGE()
  end catch
  
end
GO

create procedure sp_actBajasPartidaDet
(
  @piezasUtilizar  int
  , @idPartida     int
  , @idTipoRecorte int
  , @idProceso     int
  , @motivoBaja    varchar (100)
)
as begin
  
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   DLuna     ????/??/??   Creación
    02   DLuna     2021/07/20   Se borra parámetro @idPartidaDet para insertar bajas de partidaDet y se cambia para realizarlo
                                calculado.
  =================================================================================================================================
  */ 
  
  declare 
    @idPartidaDetAux     int
    , @noPiezasAct       int
    
  drop table if exists #TmpPartidaDet
  
  /* ------------------- */
  select
    idPartidaDet
    , noPiezasAct
  into
    #TmpPartidaDet
  from
    tb_partidaDet
  where
    idPartida = @idPartida
    and idTipoRecorte = @idTipoRecorte
    and idProceso = @idProceso - 1
    and noPiezasAct > 0
  /* ------------------- */
  
  begin try
  
    while ( ((select count(1) from #TmpPartidaDet) > 0) and @piezasUtilizar > 0)
    begin
      
      declare
        @noPiezasUpd int = 0
      
      select top 1
        @idPartidaDetAux = idPartidaDet
        , @noPiezasAct = noPiezasAct
      from
        #TmpPartidaDet
        
      --
      if (@noPiezasAct < @piezasUtilizar)
      begin
        
        set @noPiezasUpd = @noPiezasAct
        set @piezasUtilizar = @piezasUtilizar - @noPiezasAct
      end
      else if (@noPiezasAct > @piezasUtilizar)
      begin
        
        set @noPiezasUpd = @piezasUtilizar
        set @piezasUtilizar = 0
      end
      else
      begin
      
        set @noPiezasUpd = @noPiezasAct
        set @piezasUtilizar = 0
      end
      
      --
      update
        tb_partidaDet
      set
        noPiezasAct = noPiezasAct - @noPiezasUpd
      where
        idPartidaDet = @idPartidaDetAux

      --
      delete from 
        #TmpPartidaDet
      where
        idPartidaDet = @idPartidaDetAux
        
      --
      exec sp_agrBajaPartidaDet
        @noPiezas       = @noPiezasUpd
        , @motivo       = @motivoBaja
        , @idPartidaDet = @idPartidaDetAux
      
    end
    
  end try
  begin catch
    
    print 'Error al ejecutar '+ 'sp_actBajasPartidaDet ' + ERROR_MESSAGE()
  end catch
  
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_obtCueroEngraseDisp
as begin
  
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     ????/??/??   Creación
    01   DLuna     2021/10/14   Se agrupan recortes por partida y se cambia consulta con join por consulta a vista Vw_PartidaDet
  =================================================================================================================================
  */ 
  
  select
    idPartida
    , idTipoRecorte
    , [recorte] = DescripcionRecorte 
    , noPartida
    , [noPiezasAct] = sum(noPiezasAct)    
    
  from
    Vw_PartidaDet
  
  where
    idProceso = 6 -- Engrase
    and noPiezasAct > 0
  
  group by
    idPartida
    , idTipoRecorte
    , DescripcionRecorte
    , noPartida
    
end
GO

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_insInvCross
(
  @idPartida        int 
  , @idTipoRecorte  int
  , @piezasUtilizar int
)
as begin
  
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     ????/??/??   Creación
    01   DLuna     2021/10/14   Se borra parámetro @idPartidaDet para realizar bajas de partidaDet y se cambia para realizarlo
                                calculado. Se agrega parametro @idTipoRecorte
  =================================================================================================================================
  */ 
  
  declare 
    @idPartidaDetAux     int
    , @noPiezasAct       int
    
  drop table if exists #TmpPartidaDet
  
  /* ------------------- */
  select
    idPartidaDet
    , noPiezasAct
  into
    #TmpPartidaDet
  from
    tb_partidaDet
  where
    idPartida = @idPartida
    and idTipoRecorte = @idTipoRecorte
    and idProceso = 6 -- Engrase
    and noPiezasAct > 0
  /* ------------------- */
  
  begin try
  
    while ( ((select count(1) from #TmpPartidaDet) > 0) and @piezasUtilizar > 0)
    begin
      
      declare
        @noPiezasUpd int = 0
      
      select top 1
        @idPartidaDetAux = idPartidaDet
        , @noPiezasAct = noPiezasAct
      from
        #TmpPartidaDet
        
      --
      if (@noPiezasAct < @piezasUtilizar)
      begin
        
        set @noPiezasUpd = @noPiezasAct
        set @piezasUtilizar = @piezasUtilizar - @noPiezasAct
      end
      else if (@noPiezasAct > @piezasUtilizar)
      begin
        
        set @noPiezasUpd = @piezasUtilizar
        set @piezasUtilizar = 0
      end
      else
      begin
      
        set @noPiezasUpd = @noPiezasAct
        set @piezasUtilizar = 0
      end
      
      --
      update
        tb_partidaDet
      set
        noPiezasAct = noPiezasAct - @noPiezasUpd
      where
        idPartidaDet = @idPartidaDetAux

      --
      delete from 
        #TmpPartidaDet
      where
        idPartidaDet = @idPartidaDetAux
        
      --
      declare
        @kg float
        
      select
        @kg = @noPiezasUpd * (kgTotal/noPiezasTotal)
        
      from
        tb_fichaProdDet
        
      where
        idPartidaDet =@idPartidaDetAux
        
      insert into
        tb_invCross
        (
          idPartidaDet     
          , idPartida        
          , noPiezas         
          , noPiezasActuales
          , kgTotal
          , kgActual
          , fechaEntrada   
        )
      values
        (
          @idPartidaDetAux
          , @idPartida
          , @noPiezasUpd
          , @noPiezasUpd
          , @kg
          , @kg
          , getdate()
        )
      
    end
    
  end try
  begin catch
    
    print 'Error al ejecutar '+ 'sp_insInvCross ' + ERROR_MESSAGE()
  end catch
    
end
GO

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_obtEntCross
(
	@tipoRecorte varchar(20)
	, @noPartida int
	, @fecha varchar(10)
	, @fecha1 varchar(10)
  , @accion int = 0
)
as begin

  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     ????/??/??   Creación
    01   DLuna     2021/10/14   Se agrupan entradas por partida y fechaEntrada y recorte
  =================================================================================================================================
  */

  if @accion = 0
  begin
  
    select 
      p.idPartida
      , ic.fechaentrada
      , p.noPartida
      , pd.idTipoRecorte
      , tr.descripcion
      , [noPiezas] = sum(ic.noPiezas)
      , [noPiezasActuales] = sum(ic.noPiezasActuales)
      , [kgTotal] = sum(ic.kgTotal)
      , [kgActual] = sum(ic.kgActual)
      
    from 
      tb_invCross as ic
      
      inner join
        tb_partidaDet as pd
      on
        pd.idPartidaDet = ic.idPartidaDet
      
      inner join
        tb_partida as p
      on
        p.idPartida = pd.idPartida
        
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = pd.idTipoRecorte
        and tr.descripcion like @tipoRecorte
        
    where
      ic.fechaentrada between @fecha and @fecha1
      and ic.noPiezasActuales > 0
      and 
      (
        (
          @noPartida = 0
          and p.noPartida > 0
        )
        or
        (
          @noPartida > 0
          and p.noPartida = @noPartida
        )
      )
    group by
      p.idPartida
      , ic.fechaentrada
      , p.noPartida
      , pd.idTipoRecorte
      , tr.descripcion
  end
  
  else begin
    select 
      p.idPartida
      , ic.fechaentrada
      , p.noPartida
      , pd.idTipoRecorte
      , tr.descripcion
      , [noPiezas] = sum(ic.noPiezas)
      , [noPiezasActuales] = sum(ic.noPiezasActuales)
      , [kgTotal] = sum(ic.kgTotal)
      , [kgActual] = sum(ic.kgActual)
      
    from 
      tb_invCross as ic
      
      inner join
        tb_partidaDet as pd
      on
        pd.idPartidaDet = ic.idPartidaDet
      
      inner join
        tb_partida as p
      on
        p.idPartida = pd.idPartida
        
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = pd.idTipoRecorte
        and tr.descripcion like @tipoRecorte
        
    where
      ic.fechaentrada between @fecha and @fecha1
      and 
      (
        (
          @noPartida = 0
          and p.noPartida > 0
        )
        or
        (
          @noPartida > 0
          and p.noPartida = @noPartida
        )
      )
    group by
      p.idPartida
      , ic.fechaentrada
      , p.noPartida
      , pd.idTipoRecorte
      , tr.descripcion
  end
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_agrInvCrossSemi
(  
  @idPartida          int  --#01
  , @fechaentrada     date --#01
  , @idTipoRecorte    int  --#01
	, @noPiezas         int
)
as begin
  
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     ????/??/??   Creación
    01   DLuna     2021/10/14   Se borran parámetros @idInvPCross, @noPiezasActuales y @kg. Se agregan parámetros @idPartida, 
                                @fechaentrada y @idTipoRecorte para agregar invCrossSemi agrupado por partida
  =================================================================================================================================
  */ 
  
  declare 
    @idInvPCrossAux int
    , @noPiezasAct  int
    , @kgAct        float
    
  drop table if exists #TmpInvCross
  
  /* ------------------- */
  select
    ic.idInvPCross
    , ic.noPiezasActuales
    , ic.kgActual
  into
    #TmpInvCross
  from
    tb_invCross ic
    
    inner join
      tb_partidaDet as pd
    on
      pd.idPartidaDet = ic.idPartidaDet
        
  where
    ic.idPartida = @idPartida
    and pd.idTipoRecorte = @idTipoRecorte
    and ic.fechaentrada = @fechaentrada
    and ic.noPiezasActuales > 0
  /* ------------------- */
  
  begin try
  
    while ( ((select count(1) from #TmpInvCross) > 0) and @noPiezas > 0)
    begin
      
      declare
        @noPiezasUpd int = 0
      
      select top 1
        @idInvPCrossAux = idInvPCross 
        , @noPiezasAct = noPiezasActuales
        , @kgAct = kgActual
      from
        #TmpInvCross
        
      --
      if (@noPiezasAct < @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = @noPiezas - @noPiezasAct
      end
      else if (@noPiezasAct > @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezas
        set @noPiezas = 0
      end
      else
      begin
      
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = 0
      end
      
      --
      declare
        @kg float
        
      select
        @kg = @noPiezasUpd * (@kgAct/@noPiezasAct)
      
      --
      update
        tb_invCross
      set
        noPiezasActuales = noPiezasActuales - @noPiezasUpd
        , kgActual = kgActual - @kg
      where
        idInvPCross = @idInvPCrossAux
        
      --
      insert into
        tb_invCrossSemi
        
      values
        (
          @idInvPCrossAux
          , @noPiezasUpd
          , @noPiezasUpd
          , @kg
          , @kg
          , getdate()
        )
        
       --
      delete from 
        #TmpInvCross
      where
        idInvPCross = @idInvPCrossAux
      
    end
    
  end try
  begin catch
    
    print 'Error al ejecutar '+ 'sp_agrInvCrossSemi ' + ERROR_MESSAGE()
  end catch
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_agrBajaInvCross
(  
  @idPartida       int  --#01
  , @fechaentrada  date --#01
  , @idTipoRecorte int  --#01
	, @noPiezas      int
  , @motivo        varchar (100)
)
as begin
  
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     ????/??/??   Creación
    01   DLuna     2021/10/14   Se borra parámetro @idInvPCross. Se agregan parámetros @idPartida, @fechaentrada y @idTipoRecorte
                                para agregar invCrossSemi agrupado por partida
  =================================================================================================================================
  */ 
  
  declare 
    @idInvPCrossAux int
    , @noPiezasAct  int
    , @kgAct        float
    
  drop table if exists #TmpInvCross
  
  /* ------------------- */
  select
    ic.idInvPCross
    , ic.noPiezasActuales
    , ic.kgActual
  into
    #TmpInvCross
  from
    tb_invCross ic
    
    inner join
      tb_partidaDet as pd
    on
      pd.idPartidaDet = ic.idPartidaDet
        
  where
    ic.idPartida = @idPartida
    and pd.idTipoRecorte = @idTipoRecorte
    and ic.fechaentrada = @fechaentrada
    and ic.noPiezasActuales > 0
  /* ------------------- */
  
  begin try
  
    while ( ((select count(1) from #TmpInvCross) > 0) and @noPiezas > 0)
    begin
      
      declare
        @noPiezasUpd int = 0
      
      select top 1
        @idInvPCrossAux = idInvPCross 
        , @noPiezasAct = noPiezasActuales
        , @kgAct = kgActual
      from
        #TmpInvCross
        
      --
      if (@noPiezasAct < @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = @noPiezas - @noPiezasAct
      end
      else if (@noPiezasAct > @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezas
        set @noPiezas = 0
      end
      else
      begin
      
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = 0
      end
      
      --
      declare
        @kg float
        
      select
        @kg = @noPiezasUpd * (@kgAct/@noPiezasAct)
      
      --
      update
        tb_invCross
      set
        noPiezasActuales = noPiezasActuales - @noPiezasUpd
        , kgActual = kgActual - @kg
      where
        idInvPCross = @idInvPCrossAux
        
      --
      insert into
        tb_bajasInvCross
      values
        (
          @noPiezasUpd
          , @motivo
          , getdate()
          , @idInvPCrossAux
        )
        
       --
      delete from 
        #TmpInvCross
      where
        idInvPCross = @idInvPCrossAux
      
    end
    
  end try
  begin catch
    
    print 'Error al ejecutar '+ 'sp_agrBajaInvCross ' + ERROR_MESSAGE()
  end catch
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_obtInvCrossSemi
(
	@tipoRecorte varchar(20)
	, @noPartida int
	, @fecha     varchar(10)
	, @fecha1    varchar(10)
)
as begin
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     ????/??/??   Creación
    01   DLuna     2021/10/14   Se agrupan registros por partida, recorte y fechaEntrada
  =================================================================================================================================
  */ 
  
	if (@noPartida = 0)
	begin
		
    select
      idPartida
			, noPartida
      , idTipoRecorte
      , recorte
      , fechaEntrada
      , [noPiezas] = sum(noPiezas)
      , [noPiezasActuales] = sum(noPiezasActuales)
      
		from
		  Vw_InvCrossSemi
		
    where
      fechaentrada between @fecha and @fecha1
      and recorte like @tipoRecorte
      and noPiezasActuales > 0
    
    group by
      idPartida
      , noPartida
      , idTipoRecorte
      , recorte
      , fechaEntrada
	end
	
	else
	begin
		
		select
      idPartida
			, noPartida
      , idTipoRecorte
      , recorte
      , fechaEntrada
      , [noPiezas] = sum(noPiezas)
      , [noPiezasActuales] = sum(noPiezasActuales)
		from
		  Vw_InvCrossSemi
		
    where
      fechaentrada between @fecha and @fecha1
      and recorte like @tipoRecorte
      and noPartida = @noPartida
      and noPiezasActuales > 0
      
    group by
      idPartida
      , noPartida
      , idTipoRecorte
      , recorte
      , fechaEntrada
      
	end
  
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_agrInvSemi
(  
  @idPartida       int  --#01
  , @fechaentrada  date --#01
  , @idTipoRecorte int  --#01
  , @idCalibre     int
	, @idSeleccion   int
  , @noPiezas      int
	, @kgTotales     float
)
as begin
  
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     ????/??/??   Creación
    01   DLuna     2021/10/14   Se borra parámetro @idInvCrossSemi. Se agregan parámetros @idPartida, @fechaentrada y 
                                @idTipoRecorte para agregar InvSemiterminado agrupado por partida
  =================================================================================================================================
  */ 
  
  declare 
    @idInvCrossSemiAux int
    , @noPiezasAct     int
    , @kgXpieza        float
    
 --  
  select
    @kgXpieza = @kgTotales / @noPiezas
    
  drop table if exists #TmpInvCrossSemi
  
  /* ------------------- */
  select
    idInvCrossSemi
    , noPiezasActuales
  into
    #TmpInvCrossSemi
  from
    Vw_InvCrossSemi
        
  where
    idPartida = @idPartida
    and idTipoRecorte = @idTipoRecorte
    and fechaentrada = @fechaentrada
    and noPiezasActuales > 0
  /* ------------------- */
  
  begin try
  
    while ( ((select count(1) from #TmpInvCrossSemi) > 0) and @noPiezas > 0)
    begin
      
      declare
        @noPiezasUpd int = 0
      
      select top 1
        @idInvCrossSemiAux = idInvCrossSemi 
        , @noPiezasAct = noPiezasActuales
      from
        #TmpInvCrossSemi
        
      --
      if (@noPiezasAct < @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = @noPiezas - @noPiezasAct
      end
      else if (@noPiezasAct > @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezas
        set @noPiezas = 0
      end
      else
      begin
      
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = 0
      end
      
      --
      declare
        @kg float
        
      select
        @kg = @noPiezasUpd * @kgXpieza
      
      --
      update
        tb_invCrossSemi
      set
        noPiezasActuales = noPiezasActuales - @noPiezasUpd
        , kgActual = kgActual - @kg
      where
        idInvCrossSemi = @idInvCrossSemiAux
        
      --
      insert into
        tb_invSemiterminado
        
      values
        (
          @idInvCrossSemiAux
          , @idCalibre
          , @idSeleccion
          , @noPiezasUpd
          , @noPiezasUpd
          , @kg
          , @kg
          , getdate()
        )
        
       --
      delete from 
        #TmpInvCrossSemi
      where
        idInvCrossSemi = @idInvCrossSemiAux
      
    end
    
  end try
  begin catch
    
    print 'Error al ejecutar '+ 'sp_agrInvSemi ' + ERROR_MESSAGE()
  end catch
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_obtEntInvSem
(
	@tipoRecorte varchar(20)
	, @calibre   varchar(15)
	, @seleccion varchar(15)
	, @noPartida int
	, @fecha     varchar(10)
	, @fecha1    varchar(10)
)
as begin
	/*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     ????/??/??   Creación
    01   DLuna     2021/10/14   Se agrupa consulta por Partida, recorte, seleccion, calibre y fechaEntrada
  =================================================================================================================================
  */ 
  
  if (@noPartida = 0)
  begin
  
		select
      idPartida
			, noPartida
      , idTipoRecorte
      , recorte
      , idSeleccion
      , seleccion
      , idCalibre
      , calibre
      , fechaEntrada
      , [noPiezas] = sum(noPiezas)
      , [noPiezasActuales] = sum(noPiezasActuales)
      , [kgTotales] = sum(kgTotales)
      , [kgTotalesActuales] = sum(kgTotalesActuales)
      , [pesoPromXPza] = sum(PesoPromXPza)
      
    from
      Vw_InvSemiterminado        
        
    where
      recorte like @tipoRecorte
      and calibre like @calibre
      and seleccion like @seleccion
      and fechaEntrada between @fecha and @fecha1
      and noPiezasActuales > 0
    
    group by
      idPartida
			, noPartida
      , idTipoRecorte
      , recorte 
      , idSeleccion
      , seleccion
      , idCalibre
      , calibre
      , fechaEntrada
      
	end
  
  else
	begin
  
		select
      idPartida
			, noPartida
      , idTipoRecorte
      , recorte
      , idSeleccion
      , seleccion
      , idCalibre
      , calibre
      , fechaEntrada
      , [noPiezas] = sum(noPiezas)
      , [noPiezasActuales] = sum(noPiezasActuales)
      , [kgTotales] = sum(kgTotales)
      , [kgTotalesActuales] = sum(kgTotalesActuales)
      , [pesoPromXPza] = sum(PesoPromXPza)
      
    from
      Vw_InvSemiterminado        
        
    where
      recorte like @tipoRecorte
      and calibre like @calibre
      and seleccion like @seleccion
      and fechaEntrada between @fecha and @fecha1
      and noPartida = @noPartida
      and noPiezasActuales > 0
      
    group by
      idPartida
			, noPartida
      , idTipoRecorte
      , recorte 
      , idSeleccion
      , seleccion
      , idCalibre
      , calibre
      , fechaEntrada
  end

end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_agrInvSemTer
(
	@noPiezas        int
	, @kgTotales 		 float
  , @idPartida     int
  , @idTipoRecorte int
  , @idCalibre     int
  , @idSeleccion   int
  , @fechaentrada  varchar(10)
)
as begin
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     ????/??/??   Creación
    01   DLuna     2021/10/14   Se borra parámetro @idInvSemiterminado, @noPiezasActuales y se agregan parámetros @idPartida, 
                                @idTipoRecorte, @idCalibre, @idSeleccion, @fechaentrada para agrupar partidas
  =================================================================================================================================
  */ 
	declare 
    @idInvSemiterminadoAux int
    , @noPiezasAct         int
    , @kgXpieza            float
  
  --  
  select
    @kgXpieza = @kgTotales / @noPiezas
    
  drop table if exists #TmpInvSemiterminado
  
  /* ------------------- */
  select
    idInvSemiterminado
    , noPiezasActuales
  into
    #TmpInvSemiterminado
  from
    Vw_InvSemiterminado
  where
    idPartida = @idPartida
    and idTipoRecorte = @idTipoRecorte
    and idCalibre = @idCalibre
    and idSeleccion = @idSeleccion
    and fechaentrada = @fechaentrada
    and noPiezasActuales > 0
  /* ------------------- */
  
  begin try
  
    while ( ((select count(1) from #TmpInvSemiterminado) > 0) and @noPiezas > 0)
    begin
      
      declare
        @noPiezasUpd int = 0
      
      select top 1
        @idInvSemiterminadoAux = idInvSemiterminado 
        , @noPiezasAct = noPiezasActuales
      from
        #TmpInvSemiterminado
        
      --
      if (@noPiezasAct < @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = @noPiezas - @noPiezasAct
      end
      else if (@noPiezasAct > @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezas
        set @noPiezas = 0
      end
      else
      begin
      
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = 0
      end
      
      --
      declare
        @kg float
        
      select
        @kg = @noPiezasUpd * @kgXpieza
      
      --
      update
        tb_invSemiterminado
      set
        noPiezasActuales = noPiezasActuales - @noPiezasUpd
        , kgTotalesActuales = kgTotalesActuales - @kg
      where
        idInvSemiterminado = @idInvSemiterminadoAux
        
      --
      insert into
        tb_invSemTer
      values
      (
          @idInvSemiterminadoAux
          , @noPiezasUpd
          , @noPiezasUpd
          , @kg
          , getdate()
        )
        
       --
      delete from 
        #TmpInvSemiterminado
      where
        idInvSemiterminado = @idInvSemiterminadoAux
      
    end
    
  end try
  begin catch
    
    print 'Error al ejecutar '+ 'sp_agrInvSemTer ' + ERROR_MESSAGE()
  end catch
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_agrBajaInvSemiterminado
(  
  @idPartida       int
  , @idTipoRecorte int
  , @idCalibre     int
  , @idSeleccion   int
  , @fechaentrada  varchar(10)
	, @noPiezas      int
	, @motivo        varchar (100)
)
as begin
  
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     ????/??/??   Creación
    01   DLuna     2021/10/14   Se borra parámetro @idInvSemiterminado. Se agregan parámetros @idPartida, @fechaentrada,  
                                @idTipoRecorte, @idCalibre, @idSeleccion para agregar bajas de semiterminado
  =================================================================================================================================
  */ 
  
  declare 
    @idInvSemiterminadoAux int
    , @noPiezasAct         int
    , @kgAct               float
    
  drop table if exists #TmpInvSemiterminado
  
  /* ------------------- */
  select
    idInvSemiterminado
    , noPiezasActuales
    , kgTotalesActuales
  into
    #TmpInvSemiterminado
  from
    Vw_InvSemiterminado
    
  where
    idPartida = @idPartida
    and idTipoRecorte = @idTipoRecorte
    and idCalibre = @idCalibre
    and idSeleccion = @idSeleccion
    and fechaentrada = @fechaentrada
    and noPiezasActuales > 0
  /* ------------------- */
  
  begin try
  
    while ( ((select count(1) from #TmpInvSemiterminado) > 0) and @noPiezas > 0)
    begin
      
      declare
        @noPiezasUpd int = 0
      
      select top 1
        @idInvSemiterminadoAux = idInvSemiterminado 
        , @noPiezasAct = noPiezasActuales
        , @kgAct = kgTotalesActuales
      from
        #TmpInvSemiterminado
        
      --
      if (@noPiezasAct < @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = @noPiezas - @noPiezasAct
      end
      else if (@noPiezasAct > @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezas
        set @noPiezas = 0
      end
      else
      begin
      
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = 0
      end
      
      --
      declare
        @kg float
        
      select
        @kg = @noPiezasUpd * (@kgAct/@noPiezasAct)
      
      --
      update
        tb_invSemiterminado
      set
        noPiezasActuales = noPiezasActuales - @noPiezasUpd
        , kgTotalesActuales = kgTotalesActuales - @kg
      where
        idInvSemiterminado = @idInvSemiterminadoAux
        
      --
      insert into
        tb_bajasInvSemiterminado
      values
        (
          @noPiezasUpd
          , @motivo
          , getdate()
          , @idInvSemiterminadoAux
        )
        
       --
      delete from 
        #TmpInvSemiterminado
      where
        idInvSemiterminado = @idInvSemiterminadoAux
      
    end
    
  end try
  begin catch
    
    print 'Error al ejecutar '+ 'sp_agrBajaInvSemiterminado ' + ERROR_MESSAGE()
  end catch
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure sp_obtInvSemTer
as begin
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     ????/??/??   Creación
    01   DLuna     2021/10/14   Se agrupa consulta por partida, recorte, calibre, seleccion y fechaEntrada 
  =================================================================================================================================
  */ 
  
  select
	  noPartida
    , idTipoRecorte
    , tipoRecorte
    , idCalibre
    , calibre
    , idSeleccion
    , seleccion
    , fechaEntrada
    , bandera
    , [noPiezas] = sum(noPiezas)
    , [kgTotales] = sum(kgTotales)
    
  from
    Vw_InvSemTerCompleto
  
  where
    noPiezas > 0
  
  group by
    noPartida
    , idTipoRecorte
    , tipoRecorte
    , idCalibre
    , calibre
    , idSeleccion
    , seleccion
    , fechaEntrada
    , bandera
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure Usp_InvSemTerGetAgrupado
as begin
  
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     2021/10/14   Creación
  =================================================================================================================================
  */ 
  
  select
    idPartida
    , noPartida
    , idTipoRecorte
    , recorte
    , idCalibre
    , calibre
    , idSeleccion
    , seleccion
    , fechaEntrada
	  , [noPiezasActuales] = sum(noPiezasActuales)
	  
    
  from
    Vw_InvSemTer
  
  where
    noPiezasActuales > 0
  
  group by
    idPartida
    , noPartida
    , idTipoRecorte
    , recorte
    , idCalibre
    , calibre
    , idSeleccion
    , seleccion
    , fechaEntrada
    
end
GO

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure Usp_InvSemiterminadoregresar
(  
  @idPartida       int
  , @idTipoRecorte int
  , @idCalibre     int
  , @idSeleccion   int
  , @fechaentrada  varchar(10)
	, @noPiezas      int
)
as begin
  
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    00   DLuna     2021/10/14   Creación
  =================================================================================================================================
  */ 
  
  declare 
    @idInvSemTer          int
    , @idInvSemiterminado int
    , @noPiezasAct         int
    , @kgAct               float
    
  drop table if exists #TmpInvSemTer
  
  /* ------------------- */
  select
    idInvSemTer
    , idInvSemiterminado
    , noPiezasActuales
    , kgTotales
  into
    #TmpInvSemTer
  from
    Vw_InvSemTer
    
  where
    idPartida = @idPartida
    and idTipoRecorte = @idTipoRecorte
    and idCalibre = @idCalibre
    and idSeleccion = @idSeleccion
    and fechaentrada = @fechaentrada
    and noPiezasActuales > 0
  /* ------------------- */
  
  begin try
  
    while ( ((select count(1) from #TmpInvSemTer) > 0) and @noPiezas > 0)
    begin
      
      declare
        @noPiezasUpd int = 0
      
      select top 1
        @idInvSemTer = idInvSemTer 
        , @idInvSemiterminado = idInvSemiterminado
        , @noPiezasAct = noPiezasActuales
        , @kgAct = kgTotales
      from
        #TmpInvSemTer
        
      --
      if (@noPiezasAct < @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = @noPiezas - @noPiezasAct
      end
      else if (@noPiezasAct > @noPiezas)
      begin
        
        set @noPiezasUpd = @noPiezas
        set @noPiezas = 0
      end
      else
      begin
      
        set @noPiezasUpd = @noPiezasAct
        set @noPiezas = 0
      end
      
      --
      declare
        @kg float
        
      select
        @kg = @noPiezasUpd * (@kgAct/@noPiezasAct)
      
      --
      update
        tb_invSemTer
      set
        noPiezasActuales = noPiezasActuales - @noPiezasUpd
        , kgTotales = kgTotales - @kg
      where
        idInvSemTer = @idInvSemTer
        
      --
      update
        tb_invSemiterminado
      set
        noPiezasActuales = noPiezasActuales + @noPiezasUpd
        , kgTotalesActuales = kgTotalesActuales + @kg
      where
        idInvSemiterminado = @idInvSemiterminado
        
       --
      delete from 
        #TmpInvSemTer
      where
        idInvSemTer = @idInvSemTer
      
    end
    
  end try
  begin catch
    
    print 'Error al ejecutar '+ 'Usp_InvSemiterminadoregresar ' + ERROR_MESSAGE()
  end catch
end
go
