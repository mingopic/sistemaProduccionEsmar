use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtBajasInvCross')
begin 
  drop
    procedure sp_obtBajasInvCross
end
go

create procedure sp_obtBajasInvCross
as begin
    
  select
    p.noPartida
    , tr.descripcion as tipoRecorte
    , bic.noPiezas
    , bic.motivo
    , bic.fechaBaja
  
  from
    tb_bajasInvCross as bic
  inner join
    tb_invCross as ic
  on
    ic.idInvPCross = bic.idInvPCross
    
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
end
go