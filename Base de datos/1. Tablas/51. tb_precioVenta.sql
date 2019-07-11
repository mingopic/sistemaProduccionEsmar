
use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_PrecioVenta')
begin 
  drop
    table tb_PrecioVenta
end
go

create table tb_PrecioVenta
(
  idPrecioVenta     int not null identity(1,1) primary key
  , idSeleccion     int not null foreign key references tb_seleccion(idSeleccion)
  , idCalibre       int not null foreign key references tb_calibre(idCalibre)
  , idTipoRecorte   int not null foreign key references tb_tipoRecorte(idTipoRecorte)
  , precio          float
  , precio_credito	float
  , precio_original float
  , precio_buffed	float
  , idTipoMoneda    int not null foreign key references tb_tipoMoneda (idTipoMoneda)
  , idUnidadMedida  int not null foreign key references tb_unidadMedida(idUnidadMedida)
  , fecha           datetime
)
go