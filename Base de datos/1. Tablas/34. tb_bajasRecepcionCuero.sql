use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_bajasRecepcionCuero')
begin 
  drop
    table tb_bajasRecepcionCuero 
end
go

create table tb_bajasRecepcionCuero
(
  idBajaRecepcionCuero int not null identity(1,1) primary key
  , noPiezas           int
  , motivo              varchar (100)
  , fechaBaja          date
  , idRecepcionCuero   int not null foreign key references tb_recepcionCuero(idRecepcionCuero)
);