use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_tipoRecorte')
begin 
  drop
    table tb_tipoRecorte 
end
go

create table tb_tipoRecorte 
(
  idTipoRecorte int not null identity(1,1) primary key
  , descripcion varchar (30)
);
go