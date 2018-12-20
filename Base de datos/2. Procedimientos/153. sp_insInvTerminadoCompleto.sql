use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insInvTerminadoCompleto')
begin 
  drop
    procedure sp_insInvTerminadoCompleto
end
go

create procedure sp_insInvTerminadoCompleto
as begin

	delete from
    tb_invTerminadoCompleto
  
  insert into 
    tb_invTerminadoCompleto
    (
      idInvTerminado
      , bandera -- 1 foreign key de tb_invTerminadoPesado y 0 de tb_invTerminado
      , noPartida
      , idTipoRecorte
      , idCalibre
      , idSeleccion
      , noPiezasActuales
      , kgTotalesActual
      , decimetrosActual
      , piesActual
      , fechaEntrada
    )
  
  select 
    itp.idInvTerminadoPesado
    , 1
    , cp.noPartida
    , cp.idTipoRecorte
    , itp.idCalibre
    , itp.idSeleccion
    , itp.noPiezasActuales
    , itp.kgTotalesActual
    , itp.decimetrosActual
    , itp.piesActual
    , getdate()
       
  from 
    tb_invTerminadoPesado itp
  inner join
    tb_invSemTerPesado istp
  on
    istp.idInvSemTerPesado = itp.idInvSemTerPesado
    
  inner join
    tb_CueroPesado cp
  on
    cp.idInventario = istp.idInventario
    
  where
    itp.noPiezasActuales > 0
  
  -- Segundo insert en la tabla
  insert into 
    tb_invTerminadoCompleto
    (
      idInvTerminado
      , bandera -- 1 foreign key de tb_invTerminadoPesado y 0 de tb_invTerminado
      , noPartida
      , idTipoRecorte
      , idCalibre
      , idSeleccion
      , noPiezasActuales
      , kgTotalesActual
      , decimetrosActual
      , piesActual
      , fechaEntrada
    )
  
  select 
    it.idInvTerminado
    , 0
    , p.noPartida
    , pd.idTipoRecorte
    , it.idCalibre
    , it.idSeleccion
    , it.noPiezasActuales
    , it.kgTotalesActual
    , it.decimetrosActual
    , it.piesActual
    , getdate()
       
  from
    tb_invTerminado it
  inner join
    tb_invSemTer ist
  on
    ist.idInvSemTer = it.idInvSemTer

  inner join
    tb_invSemiterminado ins
  on
    ins.idInvSemiterminado = ist.idInvSemiterminado
    
  inner join
    tb_invCrossSemi ics
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
    tb_partida p
  on
    p.idPartida = pd.idPartida
    
  where
    it.noPiezasActuales > 0
end
go