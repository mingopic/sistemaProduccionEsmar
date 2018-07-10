use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_subProceso')
begin 
  drop
    table tb_subProceso 
end
go

create table tb_subProceso 
(
  idSubproceso  int not null identity(1,1) primary key
  , idProceso   int not null foreign key references tb_proceso(idProceso)
  , descripcion varchar(50)
  
  , constraint un_descripcionSubProceso unique(descripcion) 
);
go