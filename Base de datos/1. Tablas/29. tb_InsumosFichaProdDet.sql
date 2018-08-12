use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_InsumosFichaProdDet')
begin 
  drop
    table tb_InsumosFichaProdDet 
end
go

create table tb_InsumosFichaProdDet
(
  idIsumoFichaProdDet int not null identity(1,1) primary key
  , idInsumoFichaProd int not null foreign key references tb_InsumosFichaProd(idInsumoFichaProd)
  , clave             varchar(50)
  , porcentaje        float
  , Material          varchar(60)
  , Temperatura       varchar(50)
  , Rodar             varchar(50)
  , cantidad          float
  , Observaciones     varchar(100)
  , PrecioUnitario    float
  , total             float
)
go