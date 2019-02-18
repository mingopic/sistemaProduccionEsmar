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
  idInvSemTer      int
  , bandera        int -- 0 foreign key de tb_invSemTer, 1 tb_invSemTerPesado, 2 de tb_invSemTerManual
  , noPartida      int
  , idTipoRecorte  int
  , idCalibre      int
  , idSeleccion    int
  , noPiezas       int
  , kgTotales      float
  , fechaEntrada   date
)
go