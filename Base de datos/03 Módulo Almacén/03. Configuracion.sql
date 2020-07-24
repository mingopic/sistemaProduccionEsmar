

if not exists(
  select 1 
  from 
    dbo.Tb_Catalogo
  where 
    CatId in (1,2,3,4,5)
)
begin
  insert into dbo.Tb_Catalogo
  (
    CatId
    , Nombre
    , Descripcion
  )
  values
   (1, 'Tipo de material', 'Tipo de material del almacén')
  ,(2, 'Clasificación material', 'Clasificación del material en almacén')
  ,(3, 'Estatus', '')
  ,(4, 'Origen de materiales','Origen de materiales de la fórmula por subproceso de BD de Contpaq o propia')
  ,(5, 'Estatus surtido','Estatus surtido de materiales de ficha de produccion')
end
--  

/* Catálogo Tipo de Material */
if not exists(
  select 1 
  from 
    dbo.Tb_CatalogoDet
  where 
    CatDetId in (1,2,3)
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
   ( 1, 'Consumibles', 1, 1)
  ,( 2, 'Insumo', 2, 1)
  ,( 3, 'Refacciones', 3, 1)
end

/* Catálogo Clasificación material */
if not exists(
  select 1 
  from 
    dbo.Tb_CatalogoDet
  where 
    CatDetId between 4 and 24
    and CatDetId between 31 and 32
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
   ( 4, 'Acabado', 1, 2)
  ,( 5, 'Aceros Y Perfiles', 2, 2)
  ,( 6, 'Baleros', 3, 2)
  ,( 7, 'Bandas', 4, 2)
  ,( 8, 'Conexiones Galvanizadas', 5, 2)
  ,( 9, 'Conexiones PVC Y Cedula', 6, 2)
  ,( 10, 'Contactores-Auxiliares-Fusibles', 7, 2)
  ,( 11, 'E.P.P E Insumos', 8, 2)
  ,( 12, 'Farmacia', 9, 2)
  ,( 13, 'Herramientas', 10, 2)
  ,( 14, 'Laboratorio', 11, 2)
  ,( 15, 'Lijas-Piedras-Empaques', 12, 2)
  ,( 16, 'Limpieza', 13, 2)
  ,( 17, 'Mangueras Hidraulicas', 14, 2)
  ,( 18, 'Montacargas', 15, 2)
  ,( 19, 'Oficina', 16, 2)
  ,( 20, 'Pinturas Y Lubricantes', 17, 2)
  ,( 21, 'Refacciones Electricas', 19, 2)
  ,( 22, 'Refacciones Para Baños', 20, 2)
  ,( 23, 'Tornillos-Pijas-Abrazaderas', 21, 2)
  ,( 24, 'Varios', 22, 2)
  ,( 31, 'Torno y Maquinados', 23, 2)
  ,( 32, 'Químico', 18, 2)
end

/* Catálogo Estatus */
if not exists(
  select 1 
  from 
    dbo.Tb_CatalogoDet
  where 
    CatDetId in (25,26)
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
   ( 25, 'Activo', 1, 3)
  ,( 26, 'Inactivo', 2, 3)
end

/* Catálogo Origen de materiales' */
if not exists(
  select 1 
  from 
    dbo.Tb_CatalogoDet
  where 
    CatDetId in (27,28)
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
   ( 27, 'Propia', 1, 4)
  ,( 28, 'Contpaq', 2, 4)
end

/* Catálogo Estatus Surtido */
if not exists(
  select 1 
  from 
    dbo.Tb_CatalogoDet
  where 
    CatDetId in (29,30)
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
   ( 29, 'Surtida', 1, 5)
  ,( 30, 'No Surtida', 2, 5)
end

-- Unidades de medida
if not exists(
  select 1 
  from 
    dbo.tb_unidadMedida
  where 
    idUnidadMedida in (4,5,6,7,8,9)
)
begin
  insert into dbo.tb_unidadMedida
  (
    descripcion
    , desCorta
  )
  values
   ('Pieza','Pza') -- 4
   , ('Metro','M') -- 5
   , ('Par','Par') -- 6
   , ('Caja','Caja') -- 7
   , ('Litro','L') -- 8
   , ('Tambo','Tambo') -- 9
end
