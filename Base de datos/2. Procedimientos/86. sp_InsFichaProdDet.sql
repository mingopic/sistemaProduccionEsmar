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
  )
end
go