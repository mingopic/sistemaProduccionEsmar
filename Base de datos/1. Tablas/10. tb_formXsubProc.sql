use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_formXsubProc')
begin 
  drop
    table tb_formXsubProc 
end
go

create table tb_formXsubProc 
(
  idFormXSubProc  int not null identity(1,1) primary key
  , idSubproceso  int not null foreign key references tb_Subproceso(idSubProceso)
  , fechaCreacion datetime
);
go