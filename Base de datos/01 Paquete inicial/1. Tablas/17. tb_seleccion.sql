use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_seleccion')
begin 
  drop
    table tb_seleccion 
end
go

create table tb_seleccion 
(
  idSeleccion   int not null identity(1,1) primary key
  , descripcion varchar(15)
)
go