use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_inventarioCrudo')
begin 
  drop
    table tb_inventarioCrudo 
end
go

create table tb_inventarioCrudo 
(
  idInventarioCrudo  int not null identity(1,1) primary key
  , noPiezasActual   int not null
  , idRecepcionCuero int not null foreign key references tb_recepcionCuero(idRecepcionCuero)
);
go