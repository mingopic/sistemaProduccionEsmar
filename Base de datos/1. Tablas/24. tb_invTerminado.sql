use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_invTerminado')
begin 
  drop
    table tb_invTerminado 
end
go

create table tb_invTerminado
(
  idInvTerminado     int not null identity(1,1) primary key
  , idInvSemTer      int not null foreign key references tb_invSemTer(idInvSemTer)
  , idCalibre        int not null foreign key references tb_calibre(idCalibre)
  , idSeleccion      int not null foreign key references tb_seleccion(idSeleccion)
  , noPiezas				 int
  , noPiezasActuales int
  , kgTotales 		   float
  , kgTotalesActual  float
  , decimetros       float
  , decimetrosActual float
  , pies             float
  , piesActual       float
  , fechaEntrada		 date
);
go