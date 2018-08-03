use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_proveedor')
begin 
  drop
    table tb_proveedor 
end
go

create table tb_proveedor 
(
  idProveedor       int not null identity(1,1) primary key
  , nombreProveedor varchar(100)
  , estatus         int
  
  , constraint un_nombreProveedor unique(nombreProveedor) 
);
go