use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_proceso')
begin 
  drop
    table tb_proceso 
end
go

create table tb_proceso 
(
  idProceso     int not null identity(1,1) primary key
  , descripcion varchar(20)
  
  , constraint un_descripcionProceso unique(descripcion) 
);
go