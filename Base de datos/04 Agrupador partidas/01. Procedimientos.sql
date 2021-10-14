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