use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtInfoFichaProd')
begin 
  drop
    procedure sp_obtInfoFichaProd
end
go

create procedure sp_obtInfoFichaProd
(
	@idFichaProd int
)
as begin

  select
    [Proveedor] = pr.nombreProveedor + ' No. ' + cast(rc.noCamion as varchar)
    , [Tipo de Cuero] = case
        when tr.descripcion = 'Entero' then 'Entero'
        when tr.descripcion = 'Delantero Sillero' and pd.idProceso in (2,3) then 'Delantero'
        when tr.descripcion = 'Delantero Sillero' and pd.idProceso not in (2,3) then 'Delantero Sillero'
        when tr.descripcion = 'Crupon Sillero' and pd.idProceso in (2,3) then 'Crupon'
        when tr.descripcion = 'Crupon Sillero' and pd.idProceso not in (2,3) then 'Crupon Sillero'
        when tr.descripcion = 'Lados' then 'Lados'
        when tr.descripcion = 'Centro Castaño' then 'Centro Castaño'
        when tr.descripcion = 'Centro Quebracho' then 'Centro Quebracho'
        when tr.descripcion = 'Delantero Suela' then 'Delantero Suela'
      end
    , fpd.noPiezasTotal
    , fpd.kgTotal
    , [KgXPieza] = fpd.kgTotal / fpd.noPiezasTotal

  from
    tb_fichaProdDet as fpd
    
  inner join
    tb_fichaProd as fp
  on
    fp.idFichaProd = fpd.idFichaProd
	    
  inner join
    tb_partidaDet as pd
  on
    pd.idPartidaDet = fpd.idPartidaDet
    
  inner join
    tb_tipoRecorte as tr
  on
    tr.idTipoRecorte = pd.idTipoRecorte
  
  inner join
    tb_recepcionCuero rc
  on
     rc.idRecepcionCuero = pd.idRecepcionCuero
  
  inner join
      tb_proveedor pr
    on
      pr.idProveedor = rc.idProveedor
    
  where
    fpd.idFichaProd = @idFichaProd
end
go