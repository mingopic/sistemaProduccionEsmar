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
)
as begin

  select
    fp.idFichaProd
    , p.noPartida
    , pr.descripcion as proceso
    , tr.descripcion as tipoRecorte
    , fpd.noPiezasTotal
    , fpd.kgTotal
    , fpd.costoTotalCuero
    , [costoCueroXpza] = fpd.costoTotalCuero / fpd.noPiezasTotal
    , fpd.costoInsumos
    , [costoInsumoxKg] =  fpd.costoInsumos / fpd.kgTotal
    , t.nombreTambor as tambor
    , fp.fechaCreacion
    , fpd.idFichaProdDet
    
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
    
  where
    fp.fechaCreacion between @fecha and @fecha1
end
go