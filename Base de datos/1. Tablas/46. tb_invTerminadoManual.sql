use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_invTerminadoManual')
begin 
  drop
    table tb_invTerminadoManual 
end
go

create table tb_invTerminadoManual
(
  idInvTerminadoManual    int not null identity(1,1) primary key
  , idInvSemTerManual     int not null foreign key references tb_invSemTerManual(idInvSemTerManual)
  , idCalibre             int not null foreign key references tb_calibre(idCalibre)
  , idSeleccion           int not null foreign key references tb_seleccion(idSeleccion)
  , noPiezas				      int
  , noPiezasActuales      int
  , kgTotales 		        float
  , kgTotalesActual       float
  , decimetros            float
  , decimetrosActual      float
  , pies                  float
  , piesActual            float
  , fechaEntrada	        date
);
go
