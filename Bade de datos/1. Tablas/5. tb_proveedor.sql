use esmarProd
go

create table tb_proveedor 
(
  idProveedor       int not null identity(1,1) primary key
  , nombreProveedor varchar(100)
  , estatus         int
  
  , constraint un_nombreProveedor unique(nombreProveedor) 
);
go