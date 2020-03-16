use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_invSemTerPesado')
begin 
  drop
    table tb_invSemTerPesado 
end
go

create table tb_invSemTerPesado
(
  idInvSemTerPesado   int not null identity(1,1) primary key
  , idInventario      int
  , noPiezas	   	    int
  , noPiezasActuales  int
  , kgTotales 		    float
  , fechaEntrada	    date
)
go