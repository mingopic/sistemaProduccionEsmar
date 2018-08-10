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
    , @precioXpiezaRecCuero float
    , @porcentajePrecioXpza float
    , @costoCueroRecorte    float
  
  set
    @costoXkg = @costoInsumosFicha / @kgTotal
  
  set
    @costoInsumosPartida = @costoXkg * @kgPartida
  
  --Obtener precio de cada pieza entera
  select
    @precioXpiezaRecCuero = 
    (
      rc.costocamion / rc.noTotalPiezas
    )
    , @idTipoRecorte = pd.idTipoRecorte
    
  from
    idPartidaDet pd
    
    inner join
      tb_recepcionCuero rc
    on
      rc.idRecepcionCuero = pd.idRecepcionCuero
  
  where
    idPartidaDet = @idPartidaDet
  
  --Obtener el porcentaje del costo del tipo de recorte
  select
    @porcentajePrecioXpza = porcentaje
    
  from
    tb_confPrecioCuero
    
  where
    idTipoRecorte = @idTipoRecorte

  set
    @costoCueroRecorte = (@noPiezasTotal * @porcentajePrecioXpza) * @precioXpiezaRecCuero
  
  insert into
    tb_fichaProdDet
    (
      idFichaProd
      , idPartidaDet
      , noPiezasTotal
      , kgTotal
      , costoTotalCuero
      , costoInsumos
    )
    
  values
  (
    @idFichaProd
    , @idPartidaDet
    , @noPiezasPartida
    , @kgPartida
    , @costoCueroRecorte
    , @costoInsumosPartida
  )
end
go