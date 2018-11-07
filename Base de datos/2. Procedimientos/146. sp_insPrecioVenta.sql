use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insPrecioVenta')
begin 
  drop
    procedure sp_insPrecioVenta
end
go

create procedure sp_insPrecioVenta
  (
    @idSeleccion     int
    , @idCalibre     int
    , @idTipoRecorte int
    , @precio        float
  )
  as begin
  
    insert into
      tb_PrecioVenta
      (
        idSeleccion
        , idCalibre
        , idTipoRecorte
        , precio
        , fecha
      )
      
    values
      (
        @idSeleccion
        , @idCalibre
        , @idTipoRecorte
        , @precio
        , getdate()
      )
  end
go