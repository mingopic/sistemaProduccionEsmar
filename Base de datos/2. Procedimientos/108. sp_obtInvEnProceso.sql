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
    , case
        when tr.descripcion = 'Entero' then 'Entero'
        when tr.descripcion = 'Delantero Sillero' and pr.descripcion in ('REMOJO','PELAMBRE') then 'Delantero'
        when tr.descripcion = 'Delantero Sillero' and pr.descripcion not in ('REMOJO','PELAMBRE') then 'Delantero Sillero'
        when tr.descripcion = 'Crupon Sillero' and pr.descripcion in ('REMOJO','PELAMBRE') then 'Crupon'
        when tr.descripcion = 'Crupon Sillero' and pr.descripcion not in ('REMOJO','PELAMBRE') then 'Crupon Sillero'
        when tr.descripcion = 'Lados' then 'Lados'
        when tr.descripcion = 'Centro' then 'Centro'
        when tr.descripcion = 'Centro Castaño' then 'Centro Castaño'
        when tr.descripcion = 'Centro Quebracho' then 'Centro Quebracho'
        when tr.descripcion = 'Delantero Suela' then 'Delantero Suela'
      end as tipoRecorte
    , [Piezas] = pd.noPiezasAct
    , [Kg] = pd.noPiezasAct * (fpd.kgTotal/fpd.noPiezasTotal)
    , [CostoCuero] = pd.noPiezasAct * (fpd.costoTotalCuero/fpd.noPiezasTotal)
    , [CostoInsumos] = pd.noPiezasAct * (fpd.costoInsumos/fpd.noPiezasTotal)
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