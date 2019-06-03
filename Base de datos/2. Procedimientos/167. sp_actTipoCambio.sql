use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actTipoCambio')
begin 
  drop
    procedure sp_actTipoCambio
end
go

create procedure sp_actTipoCambio
  (
    @cotizacion  float 
  )

as begin
  
  update
    tb_tipoMoneda
  set
    tipoCambio = @cotizacion
    , fechaMod = getdate()
  where
    idTipoMoneda = 2 -- Dolar
  
  -- actualizar precios de venta
  update
    tb_PrecioVenta
  set
    precio = precio_original * @cotizacion
  where
    idTipoMoneda = 2 -- Dolar
    
end
go