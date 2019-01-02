use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insInvSemTerCompleto')
begin 
  drop
    procedure sp_insInvSemTerCompleto
end
go

create procedure sp_insInvSemTerCompleto
as begin

	delete from
    tb_invSemTerCompleto
  
  insert into 
    tb_invSemTerCompleto
    (
      idInvSemTer
      , bandera -- 0 foreign key de tb_invSemTer, 1 tb_invSemTerPesado, 2 de tb_invSemTerManual
      , noPartida
      , idTipoRecorte
      , idCalibre
      , idSeleccion
      , noPiezas
      , kgTotales
      , fechaEntrada
    )
  
  select 
    istp.idInvSemTerPesado
    , 1
    , cp.noPartida
    , cp.idTipoRecorte
    , cp.idCalibre
    , cp.idSeleccion
    , istp.noPiezasActuales
    , istp.kgTotales
    , istp.fechaEntrada
       
  from 
    tb_invSemTerPesado istp
    
    inner join
      tb_cueroPesado cp
    on
      cp.idInventario = istp.idInventario
      
    where
      istp.noPiezasActuales > 0
  
  -- Segundo insert en la tabla -----------------------------------------------------------------------------------------------
  insert into 
    tb_invSemTerCompleto
    (
      idInvSemTer
      , bandera -- 0 foreign key de tb_invSemTer, 1 tb_invSemTerPesado, 2 de tb_invSemTerManual
      , noPartida
      , idTipoRecorte
      , idCalibre
      , idSeleccion
      , noPiezas
      , kgTotales
      , fechaEntrada
    )
  
  select 
    ist.idInvSemTer
    , 0
    , p.noPartida
    , pd.idTipoRecorte
    , ins.idCalibre
    , ins.idSeleccion
    , ist.noPiezasActuales
    , ist.kgTotales
    , ist.fechaEntrada
       
  from 
    tb_invSemTer ist
    
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
    ist.noPiezasActuales > 0
    
  -- Tercer insert en la tabla -----------------------------------------------------------------------------------------------
  insert into 
    tb_invSemTerCompleto
    (
      idInvSemTer
      , bandera -- 0 foreign key de tb_invSemTer, 1 tb_invSemTerPesado, 2 de tb_invSemTerManual
      , noPartida
      , idTipoRecorte
      , idCalibre
      , idSeleccion
      , noPiezas
      , kgTotales
      , fechaEntrada
    )
  
  select 
    istm.idInvSemTerManual
    , 2
    , 0
    , istm.idTipoRecorte
    , istm.idCalibre
    , istm.idSeleccion
    , istm.noPiezasActuales
    , 0
    , istm.fechaEntrada
  from 
    tb_invSemTerManual istm
      
  where
    istm.noPiezasActuales > 0
    
end
go