use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_tipoCuero')
begin 
  drop
    table tb_tipoCuero 
end
go

create table tb_tipoCuero 
(
  idTipoCuero   int not null identity(1,1) primary key
  , descripcion varchar(20)
  
  , constraint un_descripcionTipoCuero unique(descripcion)
);
go