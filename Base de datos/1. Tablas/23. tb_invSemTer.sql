use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_invSemTer')
begin 
  drop
    table tb_invSemTer 
end
go

create table tb_invSemTer
(
  idInvSemTer         		   int not null identity(1,1) primary key
  , idInvSemiterminado         int not null foreign key references tb_invSemiterminado(idInvSemiterminado)
  , noPiezas				   int
  , noPiezasActuales		   int
  , kgTotales 		  		   float
  , fechaEntrada			   date
);
go