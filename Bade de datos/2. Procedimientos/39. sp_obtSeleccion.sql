use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtSeleccion')
begin 
  drop
    procedure sp_obtSeleccion
end
go


create procedure sp_obtSeleccion
as begin

	select
		*
    
	from
		tb_seleccion
end
go