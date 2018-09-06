use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_costoGarra')
begin 
  drop
    table tb_costoGarra
end
go

create table tb_costoGarra
(
  idCostoGarra int not null identity(1,1) primary key
  , costo      float
  , fecha      datetime
)
go