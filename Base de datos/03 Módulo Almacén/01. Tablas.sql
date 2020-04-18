use esmarProd
go

if object_id ('dbo.Tb_CatalogoDet') is not null
	drop table dbo.Tb_CatalogoDet
go

if object_id ('dbo.Tb_Catalogo') is not null
	drop table dbo.Tb_Catalogo
go

if object_id ('dbo.Tb_Material') is not null
	drop table dbo.Tb_Material
go

if object_id ('dbo.Tb_EntradaMaterial') is not null
	drop table dbo.Tb_EntradaMaterial
go

if object_id ('dbo.Tb_SalidaMaterial') is not null
	drop table dbo.Tb_SalidaMaterial
go

-- -----------------------------------------------------
-- Tabla dbo.Tb_Catalogo
-- -----------------------------------------------------
create table dbo.Tb_Catalogo
(
  CatId          int not null
  , Nombre       varchar (50) not null
  , Descripcion  varchar (150) default ('') not null
  , constraint Pk_Catalogo primary key (CatId)
)
go

-- -----------------------------------------------------
-- Tabla dbo.Tb_CatalogoDet
-- -----------------------------------------------------
create table dbo.Tb_Catalogodet
(
  CatDetId             int not null
  , Nombre             varchar (50) not null
  , Abreviacion        varchar (25) default ('') not null
  , OrdenVisualizacion int default ((0)) not null
  , Auxiliar           varchar (50) default ('') not null
  , FechaUltimaAct     datetime default (getdate()) not null
  , CatId              int not null
  , constraint Pk_CatalogoDet primary key (CatDetId)
  , constraint Fk_CatDet_Cat foreign key (CatId) references dbo.Tb_Catalogo (catId)
)
go

-- -----------------------------------------------------
-- Tabla dbo.Tb_Material
-- -----------------------------------------------------
create table dbo.Tb_Material
(
  MaterialId              int not null identity(1,1) primary key
  , Codigo                varchar(10) not null
  , Descripcion           varchar(100) not null
  , Existencia            float not null
  , idUnidadMedida        int not null
  , Precio                float default 0.0 not null
  , idTipoMoneda          int not null
  , CatDetTipoMaterialId  int not null
  , CatDetClasificacionId int not null
  , CatDetEstatusId       int not null
  , FechaUltimaAct        datetime default getdate() not null
  , constraint Un_Codigo  unique(Codigo)
);
go

alter table [dbo].Tb_Material with check add constraint [Fk_UnidadMedida_Material] foreign key(idUnidadMedida)
references [dbo].tb_unidadMedida (idUnidadMedida)
go

alter table [dbo].Tb_Material with check add constraint [Fk_TipoMoneda_Material] foreign key(idTipoMoneda)
references [dbo].tb_tipoMoneda (idTipoMoneda)
go

alter table [dbo].Tb_Material with check add constraint [Fk_TipoMaterial_Material] foreign key(CatDetTipoMaterialId)
references [dbo].Tb_Catalogodet (CatDetId)
go

alter table [dbo].Tb_Material with check add constraint [Fk_Clasificacion_Material] foreign key(CatDetClasificacionId)
references [dbo].Tb_Catalogodet (CatDetId)
go

alter table [dbo].Tb_Material with check add constraint [Fk_Estatus_Material] foreign key(CatDetEstatusId)
references [dbo].Tb_Catalogodet (CatDetId)
go

-- -----------------------------------------------------
-- Tabla dbo.Tb_EntradaMaterial
-- -----------------------------------------------------
create table dbo.Tb_EntradaMaterial
(
  EntradaMaterialId  int not null identity(1,1) primary key
  , MaterialId       int not null
  , Cantidad         float not null
  , Comentarios      varchar(300)
  , idUsuario        int not null
  , FechaEntrada     datetime not null
  , FechaInsercion   datetime default getdate()  
);
go

alter table [dbo].Tb_EntradaMaterial with check add constraint [Fk_Material_EntradaMaterial] foreign key(MaterialId)
references [dbo].Tb_Material (MaterialId)
go

alter table [dbo].Tb_EntradaMaterial with check add constraint [Fk_Usuario_EntradaMaterial] foreign key(idUsuario)
references [dbo].tb_usuario (idUsuario)
go

-- -----------------------------------------------------
-- Tabla dbo.Tb_SalidaMaterial
-- -----------------------------------------------------
create table dbo.Tb_SalidaMaterial
(
  SalidaMaterialId     int not null identity(1,1) primary key
  , MaterialId         int not null
  , Cantidad           float not null
  , Solicitante        varchar(100)
  , Departamento       varchar(50)
  , Comentarios        varchar(300)
  , idInsumoFichaProd  int null
  , idUsuario          int not null
  , FechaSalida        datetime not null
  , FechaInsercion     datetime default getdate()  
);
go

alter table [dbo].Tb_SalidaMaterial with check add constraint [Fk_Material_SalidaMaterial] foreign key(MaterialId)
references [dbo].Tb_Material (MaterialId)
go

alter table [dbo].Tb_SalidaMaterial with check add constraint [Fk_Usuario_SalidaMaterial] foreign key(idUsuario)
references [dbo].tb_usuario (idUsuario)
go