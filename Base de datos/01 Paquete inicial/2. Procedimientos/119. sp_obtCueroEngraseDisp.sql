use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtCueroEngraseDisp')
begin 
  drop
    procedure sp_obtCueroEngraseDisp
end
go

create procedure sp_obtCueroEngraseDisp
as begin
  
  select
    pd.idPartidaDet
    , pd.idPartida
    , pa.noPartida
    , tr.descripcion as 'recorte'
    , pd.noPiezasAct
    
  from
    tb_partidaDet pd
    
    inner join
      tb_partida pa
    on
      pa.idPartida = pd.idPartida    
      
    inner join
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = pd.idTipoRecorte
  
  where
    pd.idProceso = 6 -- Engrase
    and pd.noPiezasAct > 0
end
go