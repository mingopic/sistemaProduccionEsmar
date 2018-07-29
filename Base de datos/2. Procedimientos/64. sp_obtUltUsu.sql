use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtUltUsu')
begin 
  drop
    procedure sp_obtUltUsu
end
go

create procedure sp_obtUltUsu
  as begin
  
    select
		max(idUsuario) as idUsuario
	from
		tb_usuario
  end
go