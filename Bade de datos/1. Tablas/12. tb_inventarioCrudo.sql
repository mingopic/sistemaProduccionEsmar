use esmarProd
go

create table tb_inventarioCrudo 
(
  idInventarioCrudo  int not null identity(1,1) primary key
  , noPiezasActual   int not null
  , idRecepcionCuero int not null foreign key references tb_recepcionCuero(idRecepcionCuero)
);
go