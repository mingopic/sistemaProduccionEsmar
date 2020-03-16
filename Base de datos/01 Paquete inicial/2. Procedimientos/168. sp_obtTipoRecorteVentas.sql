use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtTipoRecorteVentas')
begin 
  drop
    procedure sp_obtTipoRecorteVentas
end
go
	
create procedure sp_obtTipoRecorteVentas
(
  @tipoMoneda varchar(10)
)
as begin
	if @tipoMoneda = 'PESO MXN'
	begin
		select
			idTipoRecorte
			, case
			  when descripcion = 'Delantero Sillero' then 'Del. Sillero Novillo/Del. Sillero Toro'
			  when descripcion = 'Crupon Sillero' then 'Crupon Sillero'
			  when descripcion = 'Centro Castaño' then 'Centro Castaño'
			  when descripcion = 'Centro Quebracho' then 'Centro Quebracho'
			  when descripcion = 'Delantero Suela' then 'Delantero Quebracho'
			  when descripcion = 'Sottopiede' then 'Sottopiede'
			  else descripcion
			end as descripcion
		
		from
			tb_tipoRecorte
		where
			not idTipoRecorte = 1 and not idTipoRecorte = 4 and not (idTipoRecorte > 7 and idTipoRecorte < 15)
	end
	
	else if @tipoMoneda = 'Dolar'
	begin
		select
			idTipoRecorte
			, case
				  when descripcion = 'Delantero Sillero' then 'Double Shoulder'
				  when descripcion = 'Crupon Sillero' then 'Double Butt'
				  when descripcion = 'Centro Castaño' then 'Single Bend Chesnut'
				  when descripcion = 'Centro Quebracho' then 'Single Bend Quebracho'
				  when descripcion = 'Delantero Suela' then 'Shoulder Quebracho'
				  when descripcion = 'Sottopiede' then 'Sottopiede'
				  else descripcion
				end as descripcion
		
		from
			tb_tipoRecorte
		where
			not idTipoRecorte = 1 and not idTipoRecorte = 4 and not (idTipoRecorte > 7 and idTipoRecorte < 15)
	end
end
go