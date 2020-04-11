
--
delete from Tb_CatalogoDet
where CatId between 1 and 24
  
delete from Tb_Catalogo
where CatId in (1,2,3)

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
  
--  

/* Catálogo Tipo de Material */
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


/* Catálogo Clasificación material */
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
,( 19, 'Papeleria', 16, 2)
,( 20, 'Pinturas Y Lubricantes', 17, 2)
,( 21, 'Refacciones Electricas', 18, 2)
,( 22, 'Refacciones Para Baños', 19, 2)
,( 23, 'Tornillos-Pijas-Abrazaderas', 20, 2)
,( 24, 'Varios', 21, 2)

/* Catálogo Estatus */
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
