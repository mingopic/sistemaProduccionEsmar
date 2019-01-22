use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_insumXproc')
begin 
  drop
    table tb_insumXproc 
end
go

create table tb_insumXproc 
(
  idInsumXProc     int not null identity(1,1) primary key
  , idFormXSubProc int not null foreign key references tb_formXsubProc(idFormXSubProc)
  , clave          varchar(50)
  , porcentaje     float
  , idInsumo       int
  , nombreProducto varchar(60) default ''
  , comentario     varchar(60)
);
go