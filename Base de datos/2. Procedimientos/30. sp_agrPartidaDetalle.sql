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
  @noPiezas        int
  , @idPartida     int
  , @idTipoRecorte int
  , @proveedor     varchar(20)
  , @noCamion      int
  , @fecha         date
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
  
  insert into
    tb_partidaDet
    (
      noPiezas
      , noPiezasAct
      , idPartida
      , idRecepcionCuero
      , idTipoRecorte
    )
    
  values
    (
      @noPiezas
      , @noPiezas
      , @idPartida
      , @idRecepcionCuero
      , @idTipoRecorte
    )
end
go