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
  , bandera        int -- 1 foreign key de tb_invSemTerPesado y 0 de tb_invSemTer
  , noPartida      int
  , idTipoRecorte  int
  , idCalibre      int
  , idSeleccion    int
  , noPiezas       int
  , kgTotales      float
  , fechaEntrada   date
)
go