use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_bajasInvSemiterminado')
begin 
  drop
    table tb_bajasInvSemiterminado 
end
go

create table tb_bajasInvSemiterminado
(
  idBajaInvSemiterminado int not null identity(1,1) primary key
  , noPiezas             int
  , motivo                varchar (100)
  , fechaBaja            date
  , idInvSemiterminado   int not null foreign key references tb_invSemiterminado(idInvSemiterminado)
);