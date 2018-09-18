use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtCostoPartida')
begin 
  drop
    procedure sp_obtCostoPartida
end
go

create procedure sp_obtCostoPartida
(
	@idProceso       int
  , @idTipoRecorte int
  , @noPartida     int
  , @anio          varchar(4)
)
as begin

  select
    pa.noPartida
    , tr.descripcion as 'TipoRecorte'
    , [Piezas] = pd.noPiezas
    , [Kg] = fpd.kgTotal
    , [CostoCuero] = fpd.costoTotalCuero
    , [CostoInsumos] = fpd.costoInsumos
    , pr.descripcion as 'Proceso'
    , pr.idProceso
    , tr.idTipoRecorte

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
    pa.noPartida = @noPartida
    and year(pa.fecha) = @anio
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
    and
    (
      (
        @idTipoRecorte = 0
      )
      or
      (
        @idTipoRecorte <> 0
        and tr.idTipoRecorte = @idTipoRecorte
      )
    )

  order by
    pr.idProceso asc

end