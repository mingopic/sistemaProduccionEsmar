use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtCalibre')
begin 
  drop
    procedure sp_obtCalibre
end
go

create procedure sp_obtCalibre
as begin

	select
		*
    
	from
		tb_calibre
end
go