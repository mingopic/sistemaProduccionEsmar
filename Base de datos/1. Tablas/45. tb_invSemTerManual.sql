use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_invSemTerManual')
begin 
  drop
    table tb_invSemTerManual 
end
go

create table tb_invSemTerManual
(
  idInvSemTerManual   int not null identity(1,1) primary key
  , idTipoRecorte     int
  , idCalibre         int
  , idSeleccion 	    int
  , noPiezas          int
  , noPiezasActuales  int
  , kgTotales         float
  , kgTotalesActual   float
  , decimetros        float
  , decimetrosActual  float
  , pies              float
  , piesActual        float
  , fechaEntrada	    date
)
go