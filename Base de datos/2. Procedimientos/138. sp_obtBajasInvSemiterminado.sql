use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtBajasInvSemiterminado')
begin 
  drop
    procedure sp_obtBajasInvSemiterminado
end
go

create procedure sp_obtBajasInvSemiterminado
as begin
    
  select
    p.noPartida
    , tr.descripcion as tipoRecorte
    , bis.noPiezas
    , bis.motivo
    , s.descripcion as seleccion
    , c.descripcion as calibre
    , bis.fechaBaja
  
  from
    tb_bajasInvSemiterminado as bis
  inner join
    tb_invSemiterminado as ins
  on
    ins.idInvSemiterminado = bis.idInvSemiterminado
  
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
    tb_seleccion as s
  on
    s.idSeleccion = ins.idSeleccion
  
  inner join
    tb_calibre as c
  on
    c.idCalibre = ins.idCalibre
end
go