use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actRolXUsu')
begin 
  drop
    procedure sp_actRolXUsu
end
go

create procedure sp_actRolXUsu
  (
    @idUsuario 		int
    , @nombreRol	varchar(100)
  )
  as begin
  
    delete from
      tb_rolesXUsuario
      
    where
		idUsuario = @idUsuario and nombreRol = @nombreRol
  end
go