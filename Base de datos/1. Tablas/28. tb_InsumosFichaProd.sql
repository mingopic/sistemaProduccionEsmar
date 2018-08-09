use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_InsumosFichaProd')
begin 
  drop
    table tb_InsumosFichaProd
end
go

create table tb_InsumosFichaProd
(
  idInsumoFichaProd int not null identity(1,1) primary key
  , idFichaProd     int not null foreign key references tb_fichaProd(idFichaProd)
  , idProceso       int not null foreign key references tb_proceso(idProceso)
  , idSubproceso    int not null foreign key references tb_subProceso(idSubproceso)
  , idFormXSubProc  int not null foreign key references tb_formXsubProc(idFormXSubProc)
  , totalInsumos    float
)
go