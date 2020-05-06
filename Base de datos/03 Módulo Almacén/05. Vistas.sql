if exists(
  select 
    *
  from 
    sys.views
  where 
    name = 'Vw_InsumosFichaProdDet' 
    and schema_id = SCHEMA_ID('dbo'))
begin
  drop view [dbo].Vw_InsumosFichaProdDet
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create view [dbo].Vw_InsumosFichaProdDet
as

  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   DLuna     2020/05/06   Creación
  =================================================================================================================================
  */
  
  select
    idInsumoFichaProd 
    , MaterialId
    , [cantidadSolicitada] = sum(cantidad)
     
  from 
    dbo.tb_InsumosFichaProdDet
    
  where 
    MaterialId not in (0,-1)
    
  group by
    idInsumoFichaProd
    , MaterialId

go

