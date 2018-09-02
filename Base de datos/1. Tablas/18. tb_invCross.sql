use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_invCross')
begin 
  drop
    table tb_invCross 
end
go

create table tb_invCross 
(
  idInvPCross        int not null identity(1,1) primary key
  , idPartidaDet     int not null foreign key references tb_partidaDet(idPartidaDet)
  , idPartida        int not null foreign key references tb_partida(idPartida)
  , noPiezas         int
  , noPiezasActuales int
  , kgTotal          float
  , kgActual         float
  , fechaEntrada     date
)
go