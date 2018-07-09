use esmarProd
go

create table tb_usuario
(
  idUsuario     int not null identity(1,1) primary key
  , usuario     varchar(15)
  , contrasenia varchar(15)
  , nombre      varchar(25)
  , tipo        varchar(10)
  
  , constraint un_usuario unique(usuario)
);
go