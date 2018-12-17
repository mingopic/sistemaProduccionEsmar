use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actPrecioVenta')
begin 
  drop
    procedure sp_actPrecioVenta
end
go

create procedure sp_actPrecioVenta
  (
    @idPrecioVenta    int
    , @idSeleccion    int
    , @idCalibre      int
    , @idTipoRecorte  int
    , @precio         float
  )
  as begin
  
    update
      tb_PrecioVenta 
      
    set
      idSeleccion = @idSeleccion
      , idCalibre = @idCalibre
      , idTipoRecorte = idTipoRecorte
      , precio = @precio
      , fecha = getdate()
      
    where
      idPrecioVenta = @idPrecioVenta
  end
go