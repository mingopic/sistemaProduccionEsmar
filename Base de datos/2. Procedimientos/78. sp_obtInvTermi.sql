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
    p.noPartida
    , tr.descripcion as tipoRecorte
    , c.descripcion as calibre
    , s.descripcion as seleccion
    , it.noPiezasActuales as noPiezas
    , it.kgTotalesActual as peso
    , it.kgTotalesActual/it.noPiezasActuales as pesoProm
    , it.decimetrosActual as decimetros
    , it.piesActual as pies
    , it.fechaEntrada
    , ((fpd.costoTotalCuero/fpd.noPiezasTotal) * it.noPiezasActuales) + ((fpd.costoInsumos/fpd.noPiezasTotal) * it.noPiezasActuales) as 'costoMateriaPrima'
    , ((fpd.costoManoObra/fpd.noPiezasTotal) * it.noPiezasActuales) as 'costoManoObra'
    , ((fpd.costoFabricacion/fpd.noPiezasTotal) * it.noPiezasActuales) as 'costoFabricacion'
    , ((fpd.costoTotalCuero/fpd.noPiezasTotal) * it.noPiezasActuales) + ((fpd.costoInsumos/fpd.noPiezasTotal) * it.noPiezasActuales) 
      + ((fpd.costoFabricacion/fpd.noPiezasTotal) * it.noPiezasActuales)
      + ((fpd.costoManoObra/fpd.noPiezasTotal) * it.noPiezasActuales) as 'costoTotal'
    , 
      (
        ( 
          select 
            pv.precio
          from 
            tb_PrecioVenta pv       
          
          where
            pv.idSeleccion = s.idSeleccion
            and pv.idCalibre = c.idCalibre
            and pv.idTipoRecorte = tr.idTipoRecorte
            and pv.fecha = 
              (
                select 
                  max(pv.fecha)
                from
                  tb_PrecioVenta pv
                where
                  pv.idSeleccion = s.idSeleccion
                  and pv.idCalibre = c.idCalibre
                  and pv.idTipoRecorte = tr.idTipoRecorte
              )
        )
        *
        (
          case
            when it.kgTotalesActual > 0 then it.kgTotalesActual
            else it.decimetrosActual
          end
        )
      )
      -
      (
        ((fpd.costoTotalCuero/fpd.noPiezasTotal) * it.noPiezasActuales) + ((fpd.costoInsumos/fpd.noPiezasTotal) * it.noPiezasActuales) 
        + ((fpd.costoFabricacion/fpd.noPiezasTotal) * it.noPiezasActuales)
        + ((fpd.costoManoObra/fpd.noPiezasTotal) * it.noPiezasActuales)
      ) as 'precioVenta'

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
    it.noPiezasActuales > 0
end