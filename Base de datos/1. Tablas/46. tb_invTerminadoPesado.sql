use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_invTerminadoPesado')
begin 
  drop
    table tb_invTerminadoPesado 
end
go

create table tb_invTerminadoPesado
(
  idInvTerminadoPesado  int not null identity(1,1) primary key
  , idInvSemTerPesado   int not null foreign key references tb_invSemTerPesado(idInvSemTerPesado)
  , idCalibre           int not null foreign key references tb_calibre(idCalibre)
  , idSeleccion         int not null foreign key references tb_seleccion(idSeleccion)
  , noPiezas				    int
  , noPiezasActuales    int
  , kgTotales 		      float
  , kgTotalesActual     float
  , decimetros          float
  , decimetrosActual    float
  , pies                float
  , piesActual          float
  , fechaEntrada	      date
);
go
