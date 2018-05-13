use master
go

if exists (select name from sys.databases WHERE name = 'esmarProd')
begin 
	drop
		database esmarProd 
end

create database esmarProd
go 

use esmarProd
go

create table tb_usuario(
	idUsuario int not null identity(1,1) primary key
	, usuario varchar(15)
	, contrasenia varchar(15)
	, nombre varchar(25)
	, tipo varchar(10)
);
go

create table tb_tipoMerma (
	idTipoMerma int not null identity(1,1) primary key
	, descripcion varchar(50)
);
go

create table tb_configMerma (
	idConfigMerma int not null identity(1,1) primary key
	, idTipoMerma int not null foreign key references tb_tipoMerma(idTipoMerma)
	, porcMermaAcep float
	, fechaConfig datetime
);
go

create table tb_tipoCuero (
    idTipoCuero int not null identity(1,1) primary key
	, descripcion varchar(20)
);
go

create table tb_proveedor (
	idProveedor int not null identity(1,1) primary key
	, nombreProveedor varchar(100)
	, estatus int
);
go

create table tb_rangoPesoCuero (
	idRangoPesoCuero int not null identity(1,1) primary key
	, rangoMin float
	, rangoMax float
	, fechaConfig datetime
);
go

create table tb_recepcionCuero (
	idRecepcionCuero int not null identity(1,1) primary key
	, idProveedor int not null foreign key references tb_proveedor(idProveedor)
	, noCamion int
	, idTipoCuero int not null foreign key references tb_tipoCuero(idTipoCuero)
	, idRangoPesoCuero int not null foreign key references tb_rangoPesoCuero(idRangoPesoCuero)
	, noPiezasLigero int
	, noPiezasPesado int
	, noTotalPiezas int
	, kgTotal float
	, precioXKilo float
	, mermaSal float
	, mermaHumedad float
	, mermaCachete float
	, mermaTarimas float
	, fechaEntrada date
);
go

create table tb_proceso (
	idProceso int not null identity(1,1) primary key
	, descripcion varchar(20)
);
go

create table tb_subProceso (
	idSubproceso int not null identity(1,1) primary key
	, idProceso int not null foreign key references tb_proceso(idProceso)
	, descripcion varchar(50)
);
go

create table tb_formXsubProc (
	idFormXSubProc int not null identity(1,1) primary key
	, idSubproceso int not null foreign key references tb_Subproceso(idSubProceso)
	, fechaCreacion datetime
);
go

create table tb_insumXproc (
	idInsumXProc int not null identity(1,1) primary key
	, idFormXSubProc int not null foreign key references tb_formXsubProc(idFormXSubProc)
	, clave varchar(10)
	, porcentaje float
	, idInsumo int
	, pu varchar(10)
);
go

-- PROCEDURES 
create procedure sp_valUsulog(@usuario varchar(15))
as begin
	select 
		* 
	from 
		tb_usuario 
	where 
		usuario=@usuario
end
go

create procedure sp_obtProv
as begin
	select 
		nombreProveedor 
	from 
		tb_proveedor 
	where 
		estatus = 1
end
go

create procedure sp_obtEntReccuero(@proveedor varchar(100), @fecha varchar(10), @fecha1 varchar(10))
as begin
	select 
		rc.idrecepcioncuero, p.nombreproveedor, tp.descripcion, rc.nocamion, rc.nototalpiezas, rc.kgtotal, rc.precioxkilo ,(rc.nototalpiezas*rc.precioxkilo) as costocamion, rc.fechaentrada
	from 
		tb_proveedor as p inner join tb_recepcioncuero as rc
		on p.idproveedor = rc.idproveedor
		inner join tb_tipocuero as tp
		on rc.idtipocuero = tp.idtipocuero
	where 
		p.nombreproveedor like @proveedor
		and rc.fechaentrada between @fecha and @fecha1
end
go
												
create procedure sp_agrFormSubProc(@idSubProceso int)
as begin
	declare @fechaCreacion datetime
	set @fechaCreacion = (select getdate())
	insert into tb_formXsubProc values (@idSubproceso,@fechaCreacion)
end
go

create procedure sp_agrInsumXProc (@idSubProceso int, @clave varchar(10), @porcentaje float, @idInsumo int)
as begin
	declare @idFormXSubProc = (select
	idFormXSubProc
	from tb_formXsubProc
	where fechaCreacion = (
	select
	max(fechaCreacion)
	from tb_formXsubProc
	where idSubProceso = @idSubProceso))
		
	insert into tb_insumXproc values (@idFormXSubProc,@clave,@porcentaje,@idInsumo)
end
go
		
create procedure sp_obtFormInsXSubProc (@idSubProceso int)
as begin
	declare @idFormXSubProc int
	set @idFormXSubProc = (select
	idFormXSubProc
	from tb_formXsubProc
	where fechaCreacion = (
	select 
	max(fechaCreacion)
	from tb_formXsubProc
	where idSubproceso = @idSubProceso))
	
	select
	*
	from tb_insumXproc
	where idFormXSubProc = @idFormXSubProc
end
go

create procedure sp_obtProc
as begin
	select
	*
	from tb_proceso
end
go

create procedure sp_obtSubProc (@idProceso)
as begin
	select
	descripcion, idSubProceso
	from tb_subProceso
	where idProceso = @idProceso
end
go

insert into tb_tipoMerma values ('SAL'),('HUMEDAD'),('CACHETE'),('TARIMAS');
