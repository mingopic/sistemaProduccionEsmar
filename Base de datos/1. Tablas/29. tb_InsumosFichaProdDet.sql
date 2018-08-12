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
  , material          varchar(60)
  , temperatura       varchar(50)
  , rodar             varchar(50)
  , cantidad          float
  , observaciones     varchar(100)
  , precioUnitario    float
  , total             float
)
go