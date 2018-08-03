use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtRecCueroEli')
begin 
  drop
    procedure sp_obtRecCueroEli
end
go

create procedure sp_obtRecCueroEli
(
	@idRecepcionCuero int
)
as begin

  select distinct
	idRecepcionCuero
    
  from
    tb_partidaDet
         
  where
    idRecepcionCuero = @idRecepcionCuero
end
go