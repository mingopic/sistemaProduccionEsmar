use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtCostoTotalPartida')
begin 
  drop
    procedure sp_obtCostoTotalPartida
end
go

create procedure sp_obtCostoTotalPartida
(
	@idProceso   int
  , @noPartida int
  , @anio      varchar(4)
)
as begin

  select
    [Piezas] = sum(pd.noPiezas)
    , [Kg] = sum(fpd.kgTotal)
    , [CostoCuero] = sum(fpd.costoTotalCuero)
    , [CostoInsumos] = sum(fpd.costoInsumos)

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

end