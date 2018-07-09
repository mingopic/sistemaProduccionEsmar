use esmarProd
go

create table tb_tipoRecorte 
(
  idTipoRecorte int not null identity(1,1) primary key
  , descripcion varchar (20)
);
go