use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_recepcionCuero')
begin 
  drop
    table tb_recepcionCuero 
end
go

create table tb_recepcionCuero 
(
  idRecepcionCuero   int not null identity(1,1) primary key
  , idProveedor      int not null foreign key references tb_proveedor(idProveedor)
  , noCamion         int
  , idTipoCuero      int not null foreign key references tb_tipoCuero(idTipoCuero)
  , idRangoPesoCuero int not null foreign key references tb_rangoPesoCuero(idRangoPesoCuero)
  , noPiezasLigero   int
  , noPiezasPesado   int
  , noTotalPiezas    int
  , kgTotal          float
  , precioXKilo      float
  , costocamion      float
  , precioGarra      float
  , mermaSal         float
  , mermaHumedad     float
  , mermaCachete     float
  , mermaTarimas     float
  , refParaMerma     int
  , idMerSal         int
  , idMerHum         int
  , idMerCac         int
  , idMerTar         int
  , origen           varchar(50)
  , fechaEntrada     date
);
go