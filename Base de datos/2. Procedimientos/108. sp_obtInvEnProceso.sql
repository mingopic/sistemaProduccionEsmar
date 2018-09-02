use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtInvEnProceso')
begin 
  drop
    procedure sp_obtInvEnProceso
end
go

create procedure sp_obtInvEnProceso
(
	@idProceso  int
)
as begin

  select
    pa.noPartida
    , tr.descripcion as 'TipoRecorte'
    , [Piezas] = pd.noPiezasAct
    , [Kg] = pd.noPiezasAct * (fpd.kgTotal/fpd.noPiezasTotal)
    , pr.descripcion as 'Proceso'
    , pr.idProceso

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
    
    inner join
      tb_proceso pr
    on
      pr.idProceso = pd.idProceso
      
    inner join
      tb_fichaProdDet fpd
    on
      fpd.idPartidaDet = pd.idPartidaDet

  where
    pd.noPiezasAct > 0
    and 
    (
      (
        @idProceso = 0
        and pd.idProceso not in (1,7)
      )
      or
      (
        @idProceso <> 0
        and pd.idProceso = @idProceso
      )
    )

  order by
    pr.idProceso asc

end