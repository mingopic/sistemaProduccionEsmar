use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_EliRecCuero')
begin 
  drop
    procedure sp_EliRecCuero
end
go

create procedure sp_EliRecCuero
(
	@idRecepcionCuero int
)
as begin

  delete from
	tb_recepcionCuero
         
  where
    idRecepcionCuero = @idRecepcionCuero
end
go