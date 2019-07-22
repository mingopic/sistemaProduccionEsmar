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

  declare @costoTotalGarras float
  declare @maxPiezas		int
  declare @maxKg			float
  
  set
    @costoTotalGarras =
      (
        select
          sum(gp.costoTotalGarras)
        from
          tb_garrasPartida as gp
        inner join
          tb_partida as p
        on
          p.idPartida = gp.idPartida
        where
          p.noPartida = @noPartida
      )
      
  if (@costoTotalGarras is null)
  begin
    set @costoTotalGarras = 0
  end
  
  
  select
    @maxPiezas = sum(pd.noPiezas)
    , @maxKg = sum(fpd.kgTotal)
  
  from
    tb_partidaDet as pd
  
  inner join
    tb_proceso as p
  on
    p.idProceso = pd.idProceso
  
  inner join
    tb_partida as pa
  on
    pa.idPartida = pd.idPartida
  
  inner join
    tb_fichaProdDet as fpd
  on
    fpd.idPartidaDet = pd.idPartidaDet
  
  where
	pd.idProceso =
	(
    select
      max(pade.idProceso)
	  from
	    tb_partidaDet as pade
    inner join
      tb_partida as par
    on
      par.idPartida = pade.idPartida
      and year(par.fecha) =@anio
      and par.noPartida = @noPartida
	  where
	    pade.idProceso < 7
	)
	and
	 pa.idPartida = 
	 (
		select 
		  idPartida
		from
		  tb_partida
	    where
		  noPartida = @noPartida
		  and year(fecha) = @anio
	 )
  

  select
    pa.noPartida
    , tr.descripcion as 'TipoRecorte'
    , [Piezas] = pd.noPiezas
    , [Kg] = fpd.kgTotal
    , case
        when pr.idProceso = 2 then fpd.costoTotalCuero
        else 0.0
      end as CostoCuero
    , [CostoInsumos] = fpd.costoInsumos
    , pr.descripcion as 'Proceso'
    , pr.idProceso
    , tr.idTipoRecorte
    , @costoTotalGarras as costoGarras
    , case
        when pr.idProceso = 6 then fpd.costoManoObra
        else 0.0
      end as costoManoObra
    , case
        when pr.idProceso = 6 then fpd.costoFabricacion
        else 0.0
      end as costoFabricacion
	, [MaxPiezas] = @maxPiezas
	, [MaxKg] = @maxKg

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