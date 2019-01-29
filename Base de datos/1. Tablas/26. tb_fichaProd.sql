use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_fichaProd')
begin 
  drop
    table tb_fichaProd 
end
go

create table tb_fichaProd
(
  idFichaProd      int not null identity(1,1) primary key
  , idTambor       int not null foreign key references tb_Tambor(idTambor)
  , noPiezasTotal  int
  , kgTotal        float
  , costoInsumos   float
  , fechaCreacion  date
  , costoManoObra  float
  , costoFabricacion float
  , idSubproceso     int not null foreign key references tb_subProceso(idSubproceso)
)
go