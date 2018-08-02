use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtInvTermi')
begin 
  drop
    procedure sp_obtInvTermi
end
go

create procedure sp_obtInvTermi
(
	@tipoRecorte varchar(20)
	, @calibre varchar(20)
	, @seleccion varchar(20)
)
as begin
	select
		tr.descripcion as tipoRecorte
		, c.descripcion as calibre
		, s.descripcion as seleccion
		, sum(it.noPiezasActuales) as noPiezas
		, sum(it.kgTotales) as peso
	    , sum(it.kgTotales)/sum(it.noPiezasActuales) as pesoProm

	from
		tb_invTerminado as it
	inner join
		tb_invSemTer as ist
	on
		ist.idInvSemTer = it.idInvSemTer
		
	inner join
		tb_invSemiterminado as ins
	on
		ins.idInvSemiterminado = ist.idInvSemiterminado
		
	inner join
		tb_invCrossSemi as ics
	on
		ics.idInvCrossSemi = ins.idInvCrossSemi

	inner join
		tb_invCross ic
	on
		ic.idInvPCross = ics.idInvPCross

	inner join
		tb_partidaDet pd
	on
		pd.idPartidaDet = ic.idPartidaDet

	inner join
		tb_tipoRecorte tr
	on
		tr.idTipoRecorte = pd.idTipoRecorte
		and tr.descripcion like @tipoRecorte

	inner join
		tb_calibre c
	on
		c.idCalibre = ins.idCalibre
		and c.descripcion like @calibre

	inner join
		tb_seleccion s
	on
		s.idSeleccion = ins.idSeleccion
		and s.descripcion like @seleccion
	
	group by
		tr.descripcion, c.descripcion, s.descripcion;
end