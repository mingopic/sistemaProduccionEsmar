use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_CueroPesado')
begin 
  drop
    table tb_CueroPesado
end
go

create table tb_CueroPesado
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