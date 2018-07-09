use esmarProd
go

create table tb_configMerma 
(
  idConfigMerma   int not null identity(1,1) primary key
  , idTipoMerma   int not null foreign key references tb_tipoMerma(idTipoMerma)
  , porcMermaAcep float
  , fechaConfig   datetime
);
go