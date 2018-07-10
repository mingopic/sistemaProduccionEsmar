use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtTipoRecorte')
begin 
  drop
    procedure sp_obtTipoRecorte
end
go
	
create procedure sp_obtTipoRecorte
as begin

	select
		*
    
	from
		tb_tipoRecorte
end
go