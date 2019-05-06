
use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_devolucion')
begin 
  drop
    table tb_devolucion
end
go

create table tb_devolucion
(
  idDevolucion     	int not null identity(1,1) primary key
  , idInvSalTerminado   int not null foreign key references tb_invSalTerminado(idInvSalTerminado)
  , noPiezas		int
  , motivo			varchar(100)
  , fecha           datetime
)
go