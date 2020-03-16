use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_InvSemiterminadoCompleto')
begin 
  drop
    table tb_InvSemiterminadoCompleto
end
go

create table tb_InvSemiterminadoCompleto
(
  idInventario       int 
  , noPartida        int
  , idTipoRecorte    int
  , idCalibre        int
  , idSeleccion      int
  , kg               float
  , noPiezas         int
  , noPiezasActuales int
  , descripcion      varchar(30)
  , fecha            date
)
go