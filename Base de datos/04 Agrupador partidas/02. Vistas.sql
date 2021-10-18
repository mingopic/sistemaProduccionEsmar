drop view if exists [dbo].[Vw_PartidaDet]
go

drop view if exists [dbo].[Vw_InvCross]
go

drop view if exists [dbo].[Vw_InvCrossSemi]
go

drop view if exists [dbo].[Vw_InvSemiterminado]
go

drop view if exists [dbo].[Vw_InvSemTerCompleto]
go

drop view if exists [dbo].[Vw_InvSemTer]
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create view [dbo].[Vw_PartidaDet]
as
 /*
 ====================================================================================================================
    #Id  Author     Date        Description
 --------------------------------------------------------------------------------------------------------------------
    00   DLuna      2021/10/14  Create
 ====================================================================================================================
 */
  
  select 
    idPartidaDet
    , noPiezas
    , noPiezasAct
    , pd.idPartida
    , pa.noPartida
    , idRecepcionCuero
    , pd.idTipoRecorte
    , [DescripcionRecorte] = tr.Descripcion
    , pd.idProceso
    , [DescripcionProceso] = pr.Descripcion
    , idInventarioCrudo
    , procedenciaCrudo
    , idRecortePartidaDet

  
  from
    tb_partidaDet pd

    inner join
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = pd.idTipoRecorte
      
    inner join
      tb_partida as pa
    on
      pa.idPartida = pd.idPartida
      
    inner join
      tb_proceso as pr
    on
      pr.idProceso = pd.idProceso
  
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create view [dbo].[Vw_InvCross]
as
 /*
 ====================================================================================================================
    #Id  Author     Date        Description
 --------------------------------------------------------------------------------------------------------------------
    00   DLuna      2021/10/14  Create
 ====================================================================================================================
 */
 
  select 
    idInvPCross
    , ic.idPartidaDet
    , ic.idPartida
    , ic.noPiezas
    , ic.noPiezasActuales
    , ic.kgTotal
    , ic.kgActual
    , tr.idTipoRecorte
    , [recorte] = tr.descripcion
    , ic.fechaEntrada
  from 
    tb_invCross ic
    
    inner join
      tb_partidaDet as pd
    on
      pd.idPartidaDet = ic.idPartidaDet
    
    inner join
      tb_tipoRecorte tr
    on
      tr.idTipoRecorte = pd.idTipoRecorte
 go
 
 /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create view [dbo].[Vw_InvCrossSemi]
as
 /*
 ====================================================================================================================
    #Id  Author     Date        Description
 --------------------------------------------------------------------------------------------------------------------
    00   DLuna      2021/10/14  Create
 ====================================================================================================================
 */
  select
    ics.idInvCrossSemi
    , ics.idInvPCross
    , ics.kgTotal
    , ics.kgActual
    , p.idPartida
    , p.noPartida
    , tr.idTipoRecorte
    , [recorte] = tr.descripcion
    , ics.noPiezas
    , ics.noPiezasActuales
    , ics.fechaEntrada
  
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
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create view [dbo].[Vw_InvSemiterminado]
as
  select  
    ins.idInvSemiterminado
    , ins.idInvCrossSemi
    , p.idPartida
    , p.noPartida
    , tr.idTipoRecorte
    , [recorte] = tr.descripcion
    , ins.noPiezas
    , ins.noPiezasActuales
    , ins.kgTotales
    , ins.kgTotalesActuales
    , [pesoPromXPza] = (ins.kgTotales/ins.noPiezas)
    , ins.idCalibre
    , [calibre] = c.descripcion
    , ins.idSeleccion
    , [seleccion] = s.descripcion
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
    
    inner join
      tb_calibre as c
    on
      c.idCalibre = ins.idCalibre
    
    inner join
      tb_seleccion as s
    on
      s.idSeleccion = ins.idSeleccion
      
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create view [dbo].[Vw_InvSemTerCompleto]
as
  select
    istc.noPartida
    , tr.idTipoRecorte
    , [tipoRecorte] = tr.descripcion
    , c.idCalibre
    , [calibre] = c.descripcion
    , s.idSeleccion
    , [seleccion] = s.descripcion
    , istc.noPiezas
    , istc.kgTotales
    , istc.fechaEntrada
    , istc.bandera
    , istc.idInvSemTer
  
  from
    tb_invSemTerCompleto as istc  
  inner join
    tb_tipoRecorte as tr
  on
    tr.idTipoRecorte = istc.idTipoRecorte
  
  inner join
    tb_calibre as c
  on
    c.idCalibre = istc.idCalibre

  inner join
    tb_seleccion s
  on
    s.idSeleccion = istc.idSeleccion
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create view [dbo].[Vw_InvSemTer]
as
 /*
 ====================================================================================================================
    #Id  Author     Date        Description
 --------------------------------------------------------------------------------------------------------------------
    00   DLuna      2021/10/14  Create
 ====================================================================================================================
 */
  select
    ist.idInvSemTer
	  , ist.idInvSemiterminado
	  , ist.noPiezas
	  , ist.noPiezasActuales
	  , ist.kgTotales
	  , ist.fechaEntrada
    , idPartida
    , noPartida
    , idTipoRecorte
    , recorte
    , idCalibre
    , calibre
    , idSeleccion
    , seleccion

  from
    tb_invSemTer ist
    
    inner join
      Vw_InvSemiterminado invSem
    on
      invSem.idInvSemiterminado = ist.idInvSemiterminado
    
go