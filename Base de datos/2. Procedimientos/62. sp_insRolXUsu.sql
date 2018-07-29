use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insRolXUsu')
begin 
  drop
    procedure sp_insRolXUsu
end
go

create procedure sp_insRolXUsu
  (
    @idUsuario 		int
    , @nombreRol	varchar(100)
  )
  as begin
  
    insert into
      tb_rolesXUsuario
      
    values
      (
        @idUsuario
        , @nombreRol
      )
  end
go