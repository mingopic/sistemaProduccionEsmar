use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_ObtDevoluciones')
begin 
  drop
    procedure sp_ObtDevoluciones
end
go

create procedure sp_ObtDevoluciones
(
  @tipoRecorte varchar(20)
  , @calibre varchar(20)
  , @seleccion varchar(20)
  , @fecha varchar(10)
  , @fecha1 varchar(10)
)
as begin
  
  select
	d.idDevolucion
    , tr.descripcion as tipoRecorte
    , d.noPiezas
    , c.descripcion as calibre
	, s.descripcion as seleccion
	, d.motivo
	, d.fecha
  
  from
    tb_devolucion d
    
    inner join
      tb_invSalTerminado isalt
    on
      isalt.idInvSalTerminado = d.idInvSalTerminado
	
	inner join
	  tb_invTerminado as it
	on
	  it.idInvTerminado = isalt.idInvTerminado
	
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
      tb_fichaProdDet fpd
    on
      fpd.idPartidaDet = pd.idPartidaDet
      
    inner join
      tb_partida p
    on
      p.idPartida = pd.idPartida

    inner join
      tb_tipoRecorte tr
    on
      tr.idTipoRecorte = pd.idTipoRecorte
      and tr.descripcion like @tipoRecorte

    inner join
      tb_calibre c
    on
      c.idCalibre = it.idCalibre
      and c.descripcion like @calibre

    inner join
      tb_seleccion s
    on
      s.idSeleccion = it.idSeleccion
      and s.descripcion like @seleccion

    where
	  d.fecha between @fecha and @fecha1
	and
      d.noPiezas > 0
end
go