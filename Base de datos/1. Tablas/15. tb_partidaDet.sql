use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_partidaDet')
begin 
  drop
    table tb_partidaDet 
end
go

create table tb_partidaDet 
(
  idPartidaDet       int not null identity(1,1) primary key
  , noPiezas         int
  , noPiezasAct      int
  , idPartida        int not null foreign key references tb_partida(idPartida)
  , idRecepcionCuero int not null foreign key references tb_recepcionCuero(idRecepcionCuero)
  , idTipoRecorte    int not null foreign key references tb_tipoRecorte(idTipoRecorte)
  , idProceso        int not null foreign key references tb_proceso(idProceso)
);
go