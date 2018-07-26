use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_usuario')
begin 
  drop
    table tb_usuario 
end
go

create table tb_usuario
(
  idUsuario     int not null identity(1,1) primary key
  , usuario     varchar(15)
  , contrasenia varchar(15)
  , nombre      varchar(25)
  , estatus		int
  
  , constraint un_usuario unique(usuario)
);
go