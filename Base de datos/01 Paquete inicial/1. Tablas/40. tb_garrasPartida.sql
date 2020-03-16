use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_garrasPartida')
begin 
  drop
    table tb_garrasPartida 
end
go

create table tb_garrasPartida 
(
  idGarrasPartida     int not null identity(1,1) primary key
  , idPartida         int not null foreign key references tb_partida(idPartida)
  , noGarras          int
  , costoTotalGarras  float
);
go