use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_invSalTerminado')
begin 
  drop
    table tb_invSalTerminado 
end
go

create table tb_invSalTerminado
(
  idInvSalTerminado int not null identity(1,1) primary key
  , bandera         int
  , idInvTerminado  int
  , idCalibre       int
  , idSeleccion     int
  , noPiezas		int
	, noPiezasActuales	int
  , kg              float
  , decimetros      float
  , pies            float
  , fechaEntrada	date
)
go