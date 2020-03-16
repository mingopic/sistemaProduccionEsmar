use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_unidadMedida')
begin 
  drop
    table tb_unidadMedida
end
go

create table tb_unidadMedida
(
  idUnidadMedida  int not null identity(1,1) primary key
  , descripcion   varchar(30)
  , desCorta      varchar(10)
)
go