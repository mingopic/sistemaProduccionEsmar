use esmarProd
go

create table tb_partidaDet 
(
  idPartidaDet       int not null identity(1,1) primary key
  , noPiezas         int
  , idPartida        int not null foreign key references tb_partida(idPartida)
  , idRecepcionCuero int not null foreign key references tb_recepcionCuero(idRecepcionCuero)
  , idTipoRecorte    int not null foreign key references tb_tipoRecorte(idTipoRecorte)
);
go