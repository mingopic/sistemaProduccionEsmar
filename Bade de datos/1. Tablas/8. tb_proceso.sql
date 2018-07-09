use esmarProd
go

create table tb_proceso 
(
  idProceso     int not null identity(1,1) primary key
  , descripcion varchar(20)
  
  , constraint un_descripcionProceso unique(descripcion) 
);
go