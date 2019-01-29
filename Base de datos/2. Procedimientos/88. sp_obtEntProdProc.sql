use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtEntProdProc')
begin 
  drop
    procedure sp_obtEntProdProc
end
go

create procedure sp_obtEntProdProc
(
  @proceso   varchar(20)
  , @recorte varchar (20)
	, @fecha   varchar(10)
	, @fecha1  varchar(10)
  , @noPartida int
  , @subproceso varchar(30)
)
as begin

  select
    fp.idFichaProd
    , p.noPartida
    , pr.descripcion as proceso
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
    , fpd.noPiezasTotal
    , fpd.kgTotal
    , fpd.costoTotalCuero
    , [costoCueroXpza] = fpd.costoTotalCuero / fpd.noPiezasTotal
    , fpd.costoInsumos
    , [costoInsumoxKg] =  fpd.costoInsumos / fpd.kgTotal
    , t.nombreTambor as tambor
    , fp.fechaCreacion
    , fpd.idFichaProdDet
    , spr.descripcion as subproceso
    
  from
    tb_fichaProdDet as fpd
    
    inner join
      tb_fichaProd as fp
    on
      fp.idFichaProd = fpd.idFichaProd
    
    inner join
      tb_tambor as t
    on
      t.idTambor = fp.idTambor
        
    inner join
      tb_partidaDet as pd
    on
      pd.idPartidaDet = fpd.idPartidaDet
      
    inner join
      tb_partida as p
    on
      p.idPartida = pd.idPartida
    
    inner join
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = pd.idTipoRecorte
      and tr.descripcion like @recorte
    
    inner join
      tb_proceso as pr
     on
      pr.idProceso = pd.idProceso
      and pr.descripcion like @proceso
    
    inner join
      tb_subProceso as spr
    on
      spr.idSubproceso = fp.idSubproceso
      and spr.descripcion like @subproceso
    
  where
    fp.fechaCreacion between @fecha and @fecha1
    and 
    (
      (
        @noPartida = 0
        and p.noPartida > 0
      )
      or
      (
        @noPartida > 0
        and p.noPartida = @noPartida
      )
    )
end
go