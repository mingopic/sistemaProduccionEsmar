use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insUsu')
begin 
  drop
    procedure sp_insUsu
end
go

create procedure sp_insUsu
  (
    @usuario 		varchar(100)
	, @contrasenia 	varchar(100)
	, @nombre 		varchar(100)
    , @estatus      int
  )
  as begin
  
    insert into
      tb_usuario 
      
    values
      (
        @usuario
		, @contrasenia
		, @nombre
        , @estatus
      )
  end
go