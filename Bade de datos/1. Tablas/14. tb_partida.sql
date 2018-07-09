use esmarProd
go

create table tb_partida 
(
  idPartida       int not null identity(1,1) primary key
  , noPartida     int
  , noTotalPiezas int
  , fecha         date
  
  , idProceso int not null foreign key references tb_proceso(idProceso)
);
go