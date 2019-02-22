use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_InsFichaProdDet')
begin 
  drop
    procedure sp_InsFichaProdDet
end
go

create procedure sp_InsFichaProdDet
  (
    @idFichaProd          int 
    , @idPartidaDet       int
    , @noPiezasPartida    int
    , @noPiezasTotal      int
    , @kgPartida          float
    , @kgTotal            float
    , @costoInsumosFicha  float
  )

as begin
  
  declare
    @idTipoRecorte          int
    , @costoXkg             float
    , @costoInsumosPartida  float
    , @idProceso            int
    , @precioXpiezaRecCuero float
    , @porcentajePrecioXpza float
    , @costoCueroRecorte    float
    , @costoGarra           float
    , @ManoObra             float
    , @costoManoObra        float
    , @gastosFabricacion     float
    , @costoGastosFabricacion float
    , @costoManoObraFicha     float
    , @costoGastosFabricacionFicha float
  
  
  
  set
    @costoXkg = @costoInsumosFicha / @kgTotal
  
  set
    @costoInsumosPartida = @costoXkg * @kgPartida
  
  
  --Obtener precio de cada pieza entera
  select
    @precioXpiezaRecCuero = 
    (
      (rc.costocamion / rc.noTotalPiezas)
    )
    , @idProceso = pd.idProceso
    , @idTipoRecorte = pd.idTipoRecorte
    , @costoGarra = rc.precioGarra
    
  from
    tb_partidaDet pd
    
    inner join
      tb_recepcionCuero rc
    on
      rc.idRecepcionCuero = pd.idRecepcionCuero
  
  where
    pd.idPartidaDet = @idPartidaDet
  
  --Obtener el porcentaje del costo del tipo de recorte
  select
    @porcentajePrecioXpza = porcentaje
    
  from
    tb_confPrecioCuero
    
  where
    idTipoRecorte = @idTipoRecorte
  
  if @idTipoRecorte in (1,4) -- Entero o Lados
  begin
  
    set 
      @costoCueroRecorte = (@noPiezasPartida * @porcentajePrecioXpza) * @precioXpiezaRecCuero
  end
  
  else begin
  
    set 
      @costoCueroRecorte = (@noPiezasPartida * @porcentajePrecioXpza) * (@precioXpiezaRecCuero - (@costoGarra * 2))
  end
  
  set
    @ManoObra = 
    (
      select
        costo
      from
        tb_confPrecioManoDeObra
      where
        idTipoRecorte = @idTipoRecorte
      and
        fecha =
        (
          select max
          (
            fecha
          )
          from
            tb_confPrecioManoDeObra
        )
    )
  
  set
    @costoManoObra = @ManoObra * @noPiezasPartida
  
  set
    @gastosFabricacion = 
    (
      select
        costo
      from
        tb_confGastosFabricacion
      where
        idTipoRecorte = @idTipoRecorte
      and
        fecha =
        (
          select max
          (
            fecha
          )
          from
            tb_confGastosFabricacion
        )
    )
    
    set
      @costoGastosFabricacion = @gastosFabricacion * @noPiezasPartida
  
  declare
    @costoInsumosAcum float
  
  -- Validar si es una ficha creada en el proceso de Remojo  
  if (@idProceso = 2)
  begin
    set @costoInsumosAcum = @costoInsumosPartida
  end
  else begin
    
    -- Aqui comienza codigo nuevo ------------------------------------------------------------------------
    declare @existe int
    
    select
      @existe = 1
    from
      fichaProdDetAux
    where
      idPartidaDet = @idPartidaDet
    
    if (@existe = 1)
    begin
      -- codigo chidori
      -- Que busque en fichaProdDetAux los datos de costos, kg, etc
    end
    
    else
    begin
      set @costoInsumosAcum = 
      (
        select 
          (costoInsumosAcum / noPiezasTotal) * @noPiezasPartida
        from
          tb_fichaProdDet
        where
          idPartidaDet = (select idRecortePartidaDet from tb_partidaDet where idPartidaDet = @idPartidaDet)
      )
    end
    -- Fin codigo Nuevo ------------------------------------------------------------------------
    
    if (@costoInsumosAcum is null)
    begin
      set
        @costoInsumosAcum = 0
    end
    
    set @costoInsumosAcum = @costoInsumosAcum + @costoInsumosPartida
  end
  
  insert into
    tb_fichaProdDet
    (
      idFichaProd
      , idPartidaDet
      , noPiezasTotal
      , kgTotal
      , costoTotalCuero
      , costoInsumos
      , costoManoObra
      , costoFabricacion
      , costoInsumosAcum
    )
    
  values
  (
    @idFichaProd
    , @idPartidaDet
    , @noPiezasPartida
    , @kgPartida
    , @costoCueroRecorte
    , @costoInsumosPartida
    , @costoManoObra
    , @costoGastosFabricacion
    , @costoInsumosAcum
  )
  
  set
    @costoManoObraFicha =
    (
      select
        costoManoObra
      from
        tb_fichaProd
      where
        idFichaProd = @idFichaProd
    )
    
  set
    @costoGastosFabricacionFicha =
    (
      select
        costoFabricacion
      from
        tb_fichaProd
      where
        idFichaProd = @idFichaProd
    )
    
  if (@costoManoObraFicha is null)
  begin
    update
      tb_fichaProd
    set
      costoManoObra = @costoManoObra
    where
      idFichaProd = @idFichaProd
  end
  
  else
  begin
    update
      tb_fichaProd
    set
      costoManoObra = costoManoObra + @costoManoObra
    where
      idFichaProd = @idFichaProd
  end
    
  if (@costoGastosFabricacionFicha is null)
  begin
    update
      tb_fichaProd
    set
      costoFabricacion = @costoGastosFabricacion
    where
      idFichaProd = @idFichaProd
  end
  
  else
  begin
    update
      tb_fichaProd
    set
      costoFabricacion = costoFabricacion + @costoGastosFabricacion
    where
      idFichaProd = @idFichaProd
  end
end
go