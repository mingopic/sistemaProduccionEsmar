use adESMAR2018
go

if exists (select name from sys.sysobjects WHERE name = 'sp_Compaq_ObtInvInsumos')
begin 
  drop
    procedure sp_Compaq_ObtInvInsumos
end
go

create procedure sp_Compaq_ObtInvInsumos
as begin
  
  select
    pr.CIDPRODUCTO
    , pr.CNOMBREPRODUCTO
    , ump.CNOMBREUNIDAD
    , (CENTRADASPERIODO12 - CSALIDASPERIODO12) as existencia
    , (CCOSTOENTRADASPERIODO12 - CCOSTOSALIDASPERIODO12) as costo
    , ((CCOSTOENTRADASPERIODO12 - CCOSTOSALIDASPERIODO12) / (CENTRADASPERIODO12 - CSALIDASPERIODO12)) as costoXUnidad
  
  from
    admProductos pr
    
    inner join
      admUnidadesMedidaPeso ump
    on
      ump.CIDUNIDAD = pr.CIDUNIDADBASE
      
    inner join
      admExistenciaCosto ec
    on
      ec.CIDPRODUCTO = pr.CIDPRODUCTO

  where
    CIDVALORCLASIFICACION6 = 44 --Insumo
  and
    ec.CIDEJERCICIO =
    (
      select max
        (CIDEJERCICIO)
      from
        admExistenciaCosto e
      
      inner join
        admProductos p
      on
        p.CIDPRODUCTO = e.CIDPRODUCTO
    )
  
  order by
    pr.CNOMBREPRODUCTO asc
end
go