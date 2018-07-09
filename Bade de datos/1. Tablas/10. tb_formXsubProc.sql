use esmarProd
go

create table tb_formXsubProc 
(
  idFormXSubProc  int not null identity(1,1) primary key
  , idSubproceso  int not null foreign key references tb_Subproceso(idSubProceso)
  , fechaCreacion datetime
);
go