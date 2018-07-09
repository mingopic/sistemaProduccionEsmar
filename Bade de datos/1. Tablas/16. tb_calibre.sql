use esmarProd
go

create table tb_calibre (
  idCalibre     int not null identity(1,1) primary key
  , descripcion varchar(15)
)
go