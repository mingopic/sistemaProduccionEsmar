use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtSalInvSemi')
begin 
  drop
    procedure sp_obtSalInvSemi
end
go

create procedure sp_obtSalInvSemi
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
		, ist.noPiezas
		, ist.kgTotales
		, (ist.kgTotales/ist.noPiezas) as pesoProm
		, s.descripcion as seleccion
		, c.descripcion as calibre
		, ist.fechaEntrada
	from
		tb_invSemTer as ist
	inner join
		tb_invSemiterminado as ins
	on
		ins.idInvSemiterminado = ist.idInvSemiterminado
		
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
		ist.fechaEntrada between @fecha and @fecha1
end
go