use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_calibre')
begin 
  drop
    table tb_calibre 
end
go

create table tb_calibre (
  idCalibre     int not null identity(1,1) primary key
  , descripcion varchar(15)
  , estatus     int
)
go