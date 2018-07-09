use esmarProd
go

create table tb_tipoMerma 
(
  idTipoMerma   int not null identity(1,1) primary key
  , descripcion varchar(50)
  
  , constraint un_descripcionMerma unique(descripcion)
);
go