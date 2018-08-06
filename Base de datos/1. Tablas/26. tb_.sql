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
  idFechaProd     int not null identity(1,1) primary key
  , idSubproceso  int not null foreign key references tb_subProceso(idSubproceso)
  , fechaEntrada  date
)
go