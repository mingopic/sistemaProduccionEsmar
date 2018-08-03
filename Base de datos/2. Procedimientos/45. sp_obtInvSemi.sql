use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtInvSemi')
begin 
  drop
    procedure sp_obtInvSemi
end
go

create procedure sp_obtInvSemi
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
		, sum(ins.noPiezasActuales) as noPiezas
		, sum(ins.kgTotales) as peso
	    , sum(ins.kgTotales)/sum(ins.noPiezasActuales) as pesoProm

	from
		tb_invSemiterminado as ins
		
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
	
	where
    ins.noPiezasActuales > 0
    
	group by
		tr.descripcion, c.descripcion, s.descripcion;
end