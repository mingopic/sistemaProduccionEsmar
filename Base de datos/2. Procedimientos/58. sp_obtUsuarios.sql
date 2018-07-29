use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtUsuarios')
begin 
  drop
    procedure sp_obtUsuarios
end
go

create procedure sp_obtUsuarios
  as begin
  
    select 
      usuario
	  , nombre
      , estatus
      
    from
      tb_usuario
  end
go