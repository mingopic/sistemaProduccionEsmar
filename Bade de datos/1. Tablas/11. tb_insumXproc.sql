use esmarProd
go

create table tb_insumXproc 
(
  idInsumXProc     int not null identity(1,1) primary key
  , idFormXSubProc int not null foreign key references tb_formXsubProc(idFormXSubProc)
  , clave          varchar(10)
  , porcentaje     float
  , idInsumo       int
);
go