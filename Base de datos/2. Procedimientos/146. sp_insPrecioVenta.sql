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
    @idSeleccion        int
    , @idCalibre        int
    , @idTipoRecorte    int
    , @precio           float
    , @idUnidadMedida   int
    , @precio_original  float
    , @idMoneda         int
	, @precio_buffed    float
	, @precio_credito	float
  )
  as begin
  
    insert into
      tb_PrecioVenta
      (
        idSeleccion
        , idCalibre
        , idTipoRecorte
        , precio
        , precio_original
        , idTipoMoneda
        , idUnidadMedida
        , fecha
		, precio_buffed
		, precio_credito
      )
      
    values
      (
        @idSeleccion
        , @idCalibre
        , @idTipoRecorte
        , @precio
        , @precio_original
        , @idMoneda
        , @idUnidadMedida
        , getdate()
		, @precio_buffed
		, @precio_credito
      )
  end
go