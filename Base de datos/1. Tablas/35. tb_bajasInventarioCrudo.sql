use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_bajasInventarioCrudo')
begin 
  drop
    table tb_bajasInventarioCrudo 
end
go

create table tb_bajasInventarioCrudo
(
  idBajaInventarioCrudo int not null identity(1,1) primary key
  , noPiezas            int
  , motivo              varchar (100)
  , fechaBaja           date
  , idInventarioCrudo   int not null foreign key references tb_inventarioCrudo(idInventarioCrudo)
);