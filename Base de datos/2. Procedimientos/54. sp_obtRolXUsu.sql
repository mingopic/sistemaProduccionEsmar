use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtRolXUsu')
begin 
  drop
    procedure sp_obtRolXUsu
end
go

create procedure sp_obtRolXUsu
(
	@idUsuario int
)
  as begin
  
    select 
		nombreRol      
    from
		tb_rolesXUsuario
	where
		idUsuario = @idUsuario
  end
go