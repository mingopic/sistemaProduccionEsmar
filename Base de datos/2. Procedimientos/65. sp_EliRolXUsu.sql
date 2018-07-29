use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_EliRolXUsu')
begin 
  drop
    procedure sp_EliRolXUsu
end
go

create procedure sp_EliRolXUsu
  (
    @idUsuario int
  )
  as begin
  
    delete from
      tb_rolesXUsuario
      
    where
		idUsuario = @idUsuario
  end
go