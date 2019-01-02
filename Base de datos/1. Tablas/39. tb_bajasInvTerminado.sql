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
  , noPiezas         int
  , motivo           varchar(200)
  , fechaBaja        date
  , idInvTerminado   int
  , bandera          int -- 0 foreign key de tb_invTerminado, 1 tb_invTerminadoPesado, 2 de tb_invTerminadoManual
);