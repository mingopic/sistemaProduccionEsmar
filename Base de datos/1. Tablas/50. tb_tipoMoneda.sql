
use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_tipoMoneda')
begin 
  drop
    table tb_tipoMonedatb_tipoMoneda
end
go

create table tb_tipoMoneda
(
  idTipoMoneda   int not null identity(1,1) primary key
  , descripcion  varchar(20)    
  , abreviacion  varchar(6)
  , tipoCambio   float
  , fechaMod     date
)
go