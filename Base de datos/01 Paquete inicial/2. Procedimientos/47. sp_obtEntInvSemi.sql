use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtEntInvSemi')
begin 
  drop
    procedure sp_obtEntInvSemi
end
go

create procedure sp_obtEntInvSemi
(
	@tipoRecorte varchar(20)
	, @calibre varchar(20)
	, @seleccion varchar(20)
	, @fecha varchar(10)
	, @fecha1 varchar(10)
)
as begin
	select
		p.noPartida
		, tr.descripcion as tipoRecorte
		, ins.noPiezas
		, ins.kgTotales
		, (ins.kgTotales/ins.noPiezas) as pesoProm
		, s.descripcion as seleccion
		, c.descripcion as calibre
		, ins.fechaEntrada
	from
		tb_invSemiterminado as ins
	inner join
		tb_invCrossSemi as ics
	on
		ics.idInvCrossSemi = ins.idInvCrossSemi
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
	inner join
		tb_calibre as c
	on
		c.idCalibre = ins.idCalibre
		and c.descripcion like @calibre
	inner join
		tb_seleccion as s
	on
		s.idSeleccion = ins.idSeleccion
		and s.descripcion like @seleccion
	where
		ins.fechaEntrada between @fecha and @fecha1
end