use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_partidaDetAux')
begin 
  drop
    table tb_partidaDetAux 
end
go

create table tb_partidaDetAux 
(
  idPartidaDetAux       int not null identity(1,1) primary key
  , idPartidaDet        int not null foreign key references tb_partidaDet(idPartidaDet)
  , kgTotal             float
  , costoTotal          float
  , costoInsumos        float
);
go