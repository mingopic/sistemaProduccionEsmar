use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_confPrecioCuero')
begin 
  drop
    table tb_confPrecioCuero
end
go

create table tb_confPrecioCuero
(
  idConfPrecioCuero int not null identity(1,1) primary key
  , idTipoRecorte   int not null foreign key references tb_tipoRecorte(idTipoRecorte)
  , porcentaje      float
)
go