use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_invCrossSemi')
begin 
  drop
    table tb_invCrossSemi 
end
go

create table tb_invCrossSemi 
(
  idInvCrossSemi     int not null identity(1,1) primary key
  , idInvPCross      int not null foreign key references tb_partidaDet(idPartidaDet)
  , noPiezas         int
  , noPiezasActuales int
  , fechaEntrada     date
)
go