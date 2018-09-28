use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_bajasPartidaDet')
begin 
  drop
    table tb_bajasPartidaDet 
end
go

create table tb_bajasPartidaDet
(
  idBajaPartidaDet int not null identity(1,1) primary key
  , noPiezas       int
  , motivo          varchar (100)
  , fechaBaja      date
  , idPartidaDet   int not null foreign key references tb_partidaDet(idPartidaDet)
);