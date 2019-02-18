use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_confGastosFabricacion')
begin 
  drop
    table tb_confGastosFabricacion
end
go

create table tb_confGastosFabricacion
(
  idConfGastosFabricacion  int not null identity(1,1) primary key
  , idTipoRecorte         int not null foreign key references tb_tipoRecorte(idTipoRecorte)
  , costo                 float
  , fecha                 datetime
)
go