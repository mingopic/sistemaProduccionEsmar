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
		, sum(ins.kg) as peso
	  , sum(ins.kg)/sum(ins.noPiezasActuales) as pesoProm

	from
		tb_InvSemiterminadoCompleto as ins

    inner join
      tb_tipoRecorte tr
    on
      tr.idTipoRecorte = ins.idTipoRecorte
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