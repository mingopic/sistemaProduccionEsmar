use esmarProd
go

/*----------------------------------------------------*/
/*
ALTER TABLE dbo.tb_formXsubProc DROP COLUMN IF EXISTS CatDetOrigenMaterialId;
GO
*/

alter table dbo.tb_formXsubProc
add CatDetOrigenMaterialId int not null default 28
go

alter table [dbo].tb_formXsubProc with check add constraint [Fk_CatDetOrigenMaterial_FormXsubProc] foreign key(CatDetOrigenMaterialId)
references [dbo].Tb_CatalogoDet (CatDetId)
go

/*----------------------------------------------------*/
alter table dbo.tb_InsumosFichaProdDet
add MaterialId int not null default -1
go

/*----------------------------------------------------*/
alter table dbo.tb_InsumosFichaProd
add CatDetEstatusSurtidoId int not null default 30
go

alter table [dbo].tb_InsumosFichaProd with check add constraint [Fk_CatDetEstatusSurtido_InsumosFichaProd] foreign key(CatDetEstatusSurtidoId)
references [dbo].Tb_CatalogoDet (CatDetId)
go

/*----------------------------------------------------*/
alter table dbo.tb_insumXproc
add MaterialId int not null default 0
go

/*----------------------------------------------------*/