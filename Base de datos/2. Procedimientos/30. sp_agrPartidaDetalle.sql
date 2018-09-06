use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrPartidaDetalle')
begin 
  drop
    procedure sp_agrPartidaDetalle
end
go

create procedure sp_agrPartidaDetalle
(
  @noPiezas     int
  , @idPartida  int
  , @recorte    varchar(20)
  , @proveedor  varchar(20)
  , @noCamion   int
  , @fecha      date
)
as begin

  declare @idRecepcionCuero int
  
  set @idRecepcionCuero =
    (
      select
        idRecepcionCuero
        
      from
        tb_recepcionCuero
        
      where
        noCamion = @noCamion
        and fechaEntrada = @fecha
        and idProveedor =
          (
            select
              idProveedor
              
            from
              tb_proveedor
              
            where
              nombreProveedor = @proveedor
          )
    )
  
  declare
    @idTipoRecorte int
  
  select
    @idTipoRecorte = idTipoRecorte
  
  from
    tb_tipoRecorte
    
  where
    descripcion = @recorte
    
  insert into
    tb_partidaDet
    (
      noPiezas
      , noPiezasAct
      , idPartida
      , idRecepcionCuero
      , idTipoRecorte
      , idProceso
    )
    
  values
    (
      @noPiezas
      , @noPiezas
      , @idPartida
      , @idRecepcionCuero
      , @idTipoRecorte
      , 1
    )
end
go