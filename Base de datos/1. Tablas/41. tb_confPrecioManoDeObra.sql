use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_confPrecioManoDeObra')
begin 
  drop
    table tb_confPrecioManoDeObra
end
go

create table tb_confPrecioManoDeObra
(
  idConfPrecioManoDeObra  int not null identity(1,1) primary key
  , idTipoRecorte         int not null foreign key references tb_tipoRecorte(idTipoRecorte)
  , costo                 float
  , fecha                 datetime
)
go