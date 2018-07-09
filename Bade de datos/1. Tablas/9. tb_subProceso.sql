use esmarProd
go

create table tb_subProceso 
(
  idSubproceso  int not null identity(1,1) primary key
  , idProceso   int not null foreign key references tb_proceso(idProceso)
  , descripcion varchar(50)
  
  , constraint un_descripcionSubProceso unique(descripcion) 
);
go