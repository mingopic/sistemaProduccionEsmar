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
    @idPrecioVenta      int
    , @precio           float
    , @precio_original  float
  )
  as begin
  
    update
      tb_PrecioVenta 
      
    set
      precio = @precio
      , precio_original = @precio_original
      , fecha = getdate()
      
    where
      idPrecioVenta = @idPrecioVenta
  end
go