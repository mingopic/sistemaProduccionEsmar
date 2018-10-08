use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtBajasInvTerminado')
begin 
  drop
    procedure sp_obtBajasInvTerminado
end
go

create procedure sp_obtBajasInvTerminado
as begin
    
  select
    p.noPartida
    , tr.descripcion as tipoRecorte
    , bint.noPiezas
    , s.descripcion as seleccion
    , c.descripcion as calibre
    , bint.motivo
    , bint.fechaBaja
  
  from
    tb_bajasInvTerminado as bint
  inner join
    tb_invTerminado as it
  on
    it.idInvTerminado = bint.idInvTerminado
  
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