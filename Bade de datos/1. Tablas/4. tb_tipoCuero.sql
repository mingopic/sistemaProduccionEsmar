use esmarProd
go

create table tb_tipoCuero 
(
  idTipoCuero   int not null identity(1,1) primary key
  , descripcion varchar(20)
  
  , constraint un_descripcionTipoCuero unique(descripcion)
);
go