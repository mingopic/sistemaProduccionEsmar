use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_configMerma')
begin 
  drop
    table tb_configMerma 
end
go

create table tb_configMerma 
(
  idConfigMerma   int not null identity(1,1) primary key
  , idTipoMerma   int not null foreign key references tb_tipoMerma(idTipoMerma)
  , porcMermaAcep float
  , fechaConfig   datetime
);
go