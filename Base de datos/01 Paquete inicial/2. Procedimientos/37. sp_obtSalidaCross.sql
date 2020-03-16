use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtSalidaCross')
begin 
  drop
    procedure sp_obtSalidaCross
end
go

create procedure sp_obtSalidaCross
(
	@tipoRecorte varchar(20)
	, @noPartida int
	, @fecha varchar(10)
	, @fecha1 varchar(10)
)
as begin

	if (@noPartida = 0)
	begin
	
		select
			p.noPartida, tr.descripcion, ics.noPiezas, ics.kgTotal, ics.fechaEntrada
		from
			tb_invCrossSemi as ics
		inner join
			tb_invCross as ic
		on
			ic.idInvPCross = ics.idInvPCross
			
		inner join
			tb_partidaDet as pd
		on
			pd.idPartidaDet = ic.idPartidaDet
		
		inner join
			tb_partida as p
		on
			p.idPartida = pd.idPartida
			
		inner join
			tb_tipoRecorte as tr
		on
			tr.idTipoRecorte = pd.idTipoRecorte
			and tr.descripcion like @tipoRecorte
			
		where
		  ics.fechaentrada between @fecha and @fecha1
	end
	
	else
	begin
		
		select
			p.noPartida, tr.descripcion, ics.noPiezas, ics.kgTotal, ics.fechaEntrada
		from
			tb_invCrossSemi as ics
		inner join
			tb_invCross as ic
		on
			ic.idInvPCross = ics.idInvPCross
			
		inner join
			tb_partidaDet as pd
		on
			pd.idPartidaDet = ic.idPartidaDet
		
		inner join
			tb_partida as p
		on
			p.idPartida = pd.idPartida
			and p.noPartida = @noPartida
			
		inner join
			tb_tipoRecorte as tr
		on
			tr.idTipoRecorte = pd.idTipoRecorte
			and tr.descripcion like @tipoRecorte
			
		where
		  ics.fechaentrada between @fecha and @fecha1
	end
end
go