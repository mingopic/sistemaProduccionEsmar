use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_bajasInvCross')
begin 
  drop
    table tb_bajasInvCross 
end
go

create table tb_bajasInvCross
(
  idBajaInvCross int not null identity(1,1) primary key
  , noPiezas     int
  , motivo        varchar (100)
  , fechaBaja    date
  , idInvPCross   int not null foreign key references tb_invCross(idInvPCross)
);