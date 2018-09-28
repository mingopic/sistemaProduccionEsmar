use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_bajasInvTerminado')
begin 
  drop
    table tb_bajasInvTerminado 
end
go

create table tb_bajasInvTerminado
(
  idBajaInvTerminado int not null identity(1,1) primary key
  , noPiezas       int
  , motivo          varchar (100)
  , fechaBaja      date
  , idInvTerminado   int not null foreign key references tb_invTerminado(idInvTerminado)
);