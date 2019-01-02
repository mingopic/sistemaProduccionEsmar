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
  
  -------------------------------------------------------------------------------------
  union all
  -------------------------------------------------------------------------------------
  
  select
    cp.noPartida
    , tr.descripcion as tipoRecorte
    , c.descripcion as calibre
    , s.descripcion as seleccion
    , itp.noPiezasActuales as noPiezas
    , itp.kgTotalesActual as peso
    , itp.kgTotalesActual/itp.noPiezasActuales as pesoProm
    , itp.decimetrosActual as decimetros
    , itp.piesActual as pies
    , itp.fechaEntrada
    , 0 as 'costoMateriaPrima'
    , 0 as 'costoManoObra'
    , 0 as 'costoFabricacion'
    , 0 as 'costoTotal'
    , 
      0 as 'precioVenta'

	from
		tb_invTerminadoPesado as itp
	
    inner join
      tb_invSemTerPesado as istp
    on
      istp.idInvSemTerPesado = itp.idInvSemTerPesado
      
    inner join
      tb_CueroPesado as cp
    on
      cp.idInventario = istp.idInventario

    inner join
      tb_tipoRecorte tr
    on
      tr.idTipoRecorte = cp.idTipoRecorte
      and tr.descripcion like @tipoRecorte

    inner join
      tb_calibre c
    on
      c.idCalibre = itp.idCalibre
      and c.descripcion like @calibre

    inner join
      tb_seleccion s
    on
      s.idSeleccion = itp.idSeleccion
      and s.descripcion like @seleccion
  
  where
    itp.noPiezasActuales > 0
  
  -------------------------------------------------------------------------------------
  union all
  -------------------------------------------------------------------------------------
  
  select
    0
    , tr.descripcion as tipoRecorte
    , c.descripcion as calibre
    , s.descripcion as seleccion
    , itm.noPiezasActuales as noPiezas
    , itm.kgTotalesActual as peso
    , itm.kgTotalesActual/itm.noPiezasActuales as pesoProm
    , itm.decimetrosActual as decimetros
    , itm.piesActual as pies
    , itm.fechaEntrada
    , 0 as 'costoMateriaPrima'
    , 0 as 'costoManoObra'
    , 0 as 'costoFabricacion'
    , 0 as 'costoTotal'
    , 
      0 as 'precioVenta'

	from
		tb_invTerminadoManual as itm
	
    inner join
      tb_invSemTerManual as istm
    on
      istm.idInvSemTerManual = itm.idInvSemTerManual

    inner join
      tb_tipoRecorte tr
    on
      tr.idTipoRecorte = istm.idTipoRecorte
      and tr.descripcion like @tipoRecorte

    inner join
      tb_calibre c
    on
      c.idCalibre = itm.idCalibre
      and c.descripcion like @calibre

    inner join
      tb_seleccion s
    on
      s.idSeleccion = itm.idSeleccion
      and s.descripcion like @seleccion
  
  where
    itm.noPiezasActuales > 0
    
end