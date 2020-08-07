use esmarProd
go

alter table dbo.Tb_EntradaMaterial
add Precio float not null default 0
go