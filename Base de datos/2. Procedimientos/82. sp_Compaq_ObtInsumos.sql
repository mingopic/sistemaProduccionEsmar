use adESMAR2018
go

if exists (select name from sys.sysobjects WHERE name = 'sp_Compaq_ObtInsumos')
begin 
  drop
    procedure sp_Compaq_ObtInsumos
end
go

create procedure sp_Compaq_ObtInsumos
as begin
  
  select
    pr.CIDPRODUCTO
    , pr.CNOMBREPRODUCTO
    , ump.CNOMBREUNIDAD
  
  from
    admProductos pr
    
    inner join
      admUnidadesMedidaPeso ump
    on
      ump.CIDUNIDAD = pr.CIDUNIDADBASE

  where
    CIDVALORCLASIFICACION6 = 44 --Insumo
  
  order by
    pr.CNOMBREPRODUCTO asc
end
go