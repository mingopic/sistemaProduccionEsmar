use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_rolesXUsuario')
begin 
  drop
    table tb_rolesXUsuario 
end
go

create table tb_rolesXUsuario
(
  idRolXUsuario       int not null identity(1,1) primary key
  , idUsuario         int not null foreign key references tb_usuario(idUsuario)
  , nombreRol 		  varchar(100)
);
go