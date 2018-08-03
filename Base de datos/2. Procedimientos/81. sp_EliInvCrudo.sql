use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_EliInvCrudo')
begin 
  drop
    procedure sp_EliInvCrudo
end
go

create procedure sp_EliInvCrudo
(
	@idRecepcionCuero int
)
as begin

  delete from
	tb_inventarioCrudo
         
  where
    idRecepcionCuero = @idRecepcionCuero
end
go