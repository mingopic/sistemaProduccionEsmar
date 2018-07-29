use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actUsu')
begin 
  drop
    procedure sp_actUsu
end
go

create procedure sp_actUsu
  (
	@usuario		    varchar(100)
    , @contrasenia      varchar(100)
    , @nombre   		varchar(100)
	, @estatus 			int
	, @idUsuario 		int
  )
  as begin
  
    update
      tb_usuario 
      
    set
	  usuario = @usuario
      , contrasenia = @contrasenia
	  , nombre = @nombre
	  , estatus = @estatus
      
    where
      idUsuario = @idUsuario
  end
go