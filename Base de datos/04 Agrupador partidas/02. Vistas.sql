drop view if exists [dbo].[Vw_PartidaDet]
go

drop view if exists [dbo].[Vw_InvCross]
go

drop view if exists [dbo].[Vw_InvCrossSemi]
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