use esmarProd
go

create table tb_invSemiterminado 
(
  idInvSemiterminado int not null identity(1,1) primary key
  , idInvCrossSemi   int not null foreign key references tb_invCrossSemi(idInvCrossSemi)
  , idCalibre        int not null foreign key references tb_calibre(idCalibre)
  , idSeleccion      int not null foreign key references tb_seleccion(idSeleccion)
  , noPiezas         int
  , noPiezasActuales int
  , kgTotales        float
  , fechaEntrada     date
)
go