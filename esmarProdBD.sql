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

CREATE PROC consultaRecepcionCuero(@proveedor VARCHAR(100), @fecha VARCHAR(100), @fecha1 VARCHAR(100))
AS BEGIN
	SELECT 
		rc.idRecepcionCuero, p.nombreProveedor, tp.descripcion, rc.noCamion, rc.noTotalPiezas, rc.kgTotal, rc.precioXKilo ,(rc.noTotalPiezas*rc.precioXKilo) AS costoCamion, rc.fechaEntrada
	FROM 
		tb_proveedor AS p INNER JOIN tb_recepcionCuero AS rc
		ON p.idProveedor = rc.idProveedor
		INNER JOIN tb_tipoCuero AS tp
		ON rc.idTipoCuero = tp.idTipoCuero
	WHERE 
		p.nombreProveedor LIKE @proveedor
		AND rc.fechaEntrada BETWEEN @fecha AND @fecha1
END

CREATE PROC login(@usuario VARCHAR(100))
AS BEGIN
	SELECT 
		* 
	FROM 
		tb_usuario 
	WHERE 
		usuario=@usuario
END
