use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_invSemTerCompleto')
begin 
  drop
    table tb_invSemTerCompleto 
end
go

create table tb_invSemTerCompleto 
(
  idInvSemTerCompleto   int not null identity(1,1) primary key
  , noPartida           int
  , idTipoRecorte       int
  , idCalibre           int
  , idSeleccion         int
  , noPiezas            int
  , noPiezasActuales    int
  , kgTotales           float
  , kgTotalesActuales   float
  , fechaEntrada        date
)
go