-- Unidades de medida
if not exists(
  select 1 
  from 
    dbo.tb_unidadMedida
  where 
    idUnidadMedida in (10)
)
begin
  insert into dbo.tb_unidadMedida
  (
    descripcion
    , desCorta
  )
  values
   ('Servicio','Servicio') -- 10
end


/* Cat�logo Clasificaci�n material */
if not exists(
  select 1 
  from 
    dbo.Tb_CatalogoDet
  where 
    CatDetId in (33,34,35,36)
)
begin
  insert into dbo.Tb_CatalogoDet
  (
    CatDetId
    , Nombre
    , OrdenVisualizacion
    , CatId
  )
  values
   ( 33, 'Materiales de construcci�n', 24, 2)
   , ( 34, 'Papeleria', 25, 2)
   , ( 35, 'Refacciones para montacargas', 26, 2)
   , ( 36, 'Tornillos-Pijas-Conexiones neumaticas', 27, 2)
end