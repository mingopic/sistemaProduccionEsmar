
use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_tipoRecorte_ingles')
begin 
  drop
    table tb_tipoRecorte_ingles
end
go

create table tb_tipoRecorte_ingles
(
  idTipoRecorte_ingles  int not null identity(1,1) primary key
  , descripcion varchar (50)
  , idTipoRecorte       int not null foreign key references tb_tipoRecorte(idTipoRecorte)
)
go