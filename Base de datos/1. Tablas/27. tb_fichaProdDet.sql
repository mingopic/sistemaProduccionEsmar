use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_fichaProdDet')
begin 
  drop
    table tb_fichaProdDet 
end
go

create table tb_fichaProdDet
(
  idFichaProdDet     int not null identity(1,1) primary key
  , idFichaProd      int not null foreign key references tb_fichaProd(idFichaProd)
  , idPartidaDet     int not null foreign key references tb_partidaDet(idPartidaDet)
  , noPiezasTotal    int
  , kgTotal          float
  , costoTotalCuero  float
  , costoInsumos     float
  , costoManoObra    float
  , costoFabricacion float
  , costoInsumosAcum float
)
go