use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insInvSemiCompleto')
begin 
  drop
    procedure sp_insInvSemiCompleto
end
go

create procedure sp_insInvSemiCompleto
(
	@tipoRecorte varchar(20)
	, @calibre varchar(20)
	, @seleccion varchar(20)
)
as begin
  
  delete from
    tb_InvSemiterminadoCompleto
  
  insert into 
    tb_InvSemiterminadoCompleto
    (
      idInventario
      , noPartida
      , idTipoRecorte
      , idCalibre
      , idSeleccion
      , kg
      , noPiezas
      , noPiezasActuales
      , descripcion
      , fecha
    )
  
  select 
    cp.idInventario
    , cp.noPartida
    , cp.idTipoRecorte
    , cp.idCalibre
    , cp.idSeleccion
    , cp.kg
    , cp.noPiezas
    , cp.noPiezasActuales
    , cp.descripcion
    , cp.fecha
    
  from 
    tb_CueroPesado cp
    
    inner join 
      tb_tipoRecorte tr
    on
      tr.idTipoRecorte = cp.idTipoRecorte
      and tr.descripcion like @tipoRecorte
    
    inner join
      tb_calibre ca
    on
      ca.idCalibre = cp.idCalibre
      and ca.descripcion like @calibre
    
    inner join
      tb_seleccion se
    on
      se.idSeleccion = cp.idSeleccion
      and se.descripcion like @seleccion
  
  where
    cp.noPiezasActuales > 0
  
  -- insertando los datos actuales de la tabla tb_invSemiterminado
  insert into 
    tb_InvSemiterminadoCompleto
    (
      idInventario
      , noPartida
      , idTipoRecorte
      , idCalibre
      , idSeleccion
      , kg
      , noPiezas
      , noPiezasActuales
      , descripcion
      , fecha
    )
  
  select
    ins.idInvSemiterminado
		, p.noPartida
		, pd.idTipoRecorte
		, ins.idCalibre
		, ins.idSeleccion
		, ins.kgTotalesActuales
		, ins.noPiezas
		, ins.noPiezasActuales
    , ''
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
      and tr.descripcion like @tipoRecorte
      
    inner join
      tb_calibre as c
    on
      c.idCalibre = ins.idCalibre
      and c.descripcion like @calibre
      
    inner join
      tb_seleccion as s
    on
      s.idSeleccion = ins.idSeleccion
      and s.descripcion like @seleccion
	
	where
    ins.noPiezasActuales > 0
end