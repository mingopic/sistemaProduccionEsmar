-- --------------------------------------------------------
-- Host:                         192.168.0.3
-- Versión del servidor:         10.0.17-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win32
-- HeidiSQL Versión:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para esmar_t
CREATE DATABASE IF NOT EXISTS `esmar_t` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `esmar_t`;

-- Volcando estructura para tabla esmar_t.calibrar
CREATE TABLE IF NOT EXISTS `calibrar` (
  `idCalibrar` int(11) NOT NULL AUTO_INCREMENT,
  `idCueroTrabajar` int(11) NOT NULL,
  `idTipoCalibre` int(11) NOT NULL,
  `noPiezas` int(11) DEFAULT NULL,
  `noPiezasActuales` int(11) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idCalibrar`),
  KEY `idCueroTrabajar` (`idCueroTrabajar`),
  KEY `idTipoCalibre` (`idTipoCalibre`),
  CONSTRAINT `calibrar_ibfk_1` FOREIGN KEY (`idCueroTrabajar`) REFERENCES `cuerotrabajar` (`idCueroTrabajar`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `calibrar_ibfk_2` FOREIGN KEY (`idTipoCalibre`) REFERENCES `tipocalibre` (`idTipoCalibre`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.calibrar: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `calibrar` DISABLE KEYS */;
/*!40000 ALTER TABLE `calibrar` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.catalogoproducto
CREATE TABLE IF NOT EXISTS `catalogoproducto` (
  `idCatalogoProducto` int(11) NOT NULL AUTO_INCREMENT,
  `idTipoProducto` int(11) NOT NULL,
  `idTipoCalibre` int(11) NOT NULL,
  `idSeleccion` int(11) NOT NULL,
  `idMoneda` int(11) NOT NULL,
  `unidad` varchar(10) DEFAULT NULL,
  `precioNco` float DEFAULT NULL,
  `precioAco` float DEFAULT NULL,
  `precioNcr` float DEFAULT NULL,
  `precioAcr` float DEFAULT NULL,
  PRIMARY KEY (`idCatalogoProducto`),
  KEY `idTipoProducto` (`idTipoProducto`),
  KEY `idTipoCalibre` (`idTipoCalibre`),
  KEY `idSeleccion` (`idSeleccion`),
  KEY `idMoneda` (`idMoneda`),
  CONSTRAINT `catalogoproducto_ibfk_1` FOREIGN KEY (`idTipoProducto`) REFERENCES `tipoproductocuero` (`idTipoProducto`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `catalogoproducto_ibfk_2` FOREIGN KEY (`idTipoCalibre`) REFERENCES `tipocalibre` (`idTipoCalibre`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `catalogoproducto_ibfk_3` FOREIGN KEY (`idSeleccion`) REFERENCES `seleccioncuero` (`idSeleccion`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `catalogoproducto_ibfk_4` FOREIGN KEY (`idMoneda`) REFERENCES `moneda` (`idMoneda`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.catalogoproducto: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `catalogoproducto` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalogoproducto` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.cliente
CREATE TABLE IF NOT EXISTS `cliente` (
  `idCliente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(40) DEFAULT NULL,
  `calle` varchar(40) DEFAULT NULL,
  `colonia` varchar(40) DEFAULT NULL,
  `noInterior` varchar(10) DEFAULT NULL,
  `noExterior` varchar(10) DEFAULT NULL,
  `contacto` varchar(40) DEFAULT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `correo` varchar(60) DEFAULT NULL,
  `rfc` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`idCliente`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.cliente: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.cueropesado
CREATE TABLE IF NOT EXISTS `cueropesado` (
  `idcueroPesado` int(11) NOT NULL AUTO_INCREMENT,
  `idcueroSeleccionado` int(11) NOT NULL,
  `peso` float DEFAULT NULL,
  `pesoActual` float DEFAULT NULL,
  `noPiezas` int(11) DEFAULT NULL,
  `noPiezasActuales` int(11) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idcueroPesado`),
  KEY `idcueroSeleccionado` (`idcueroSeleccionado`),
  CONSTRAINT `cueropesado_ibfk_1` FOREIGN KEY (`idcueroSeleccionado`) REFERENCES `cueroseleccionado` (`idcueroSeleccionado`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.cueropesado: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `cueropesado` DISABLE KEYS */;
/*!40000 ALTER TABLE `cueropesado` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.cueroseleccionado
CREATE TABLE IF NOT EXISTS `cueroseleccionado` (
  `idcueroSeleccionado` int(11) NOT NULL AUTO_INCREMENT,
  `idCalibrar` int(11) NOT NULL,
  `idSeleccion` int(11) NOT NULL,
  `noPiezas` int(11) DEFAULT NULL,
  `noPiezasActuales` int(11) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idcueroSeleccionado`),
  KEY `idCalibrar` (`idCalibrar`),
  KEY `idSeleccion` (`idSeleccion`),
  CONSTRAINT `cueroseleccionado_ibfk_1` FOREIGN KEY (`idCalibrar`) REFERENCES `calibrar` (`idCalibrar`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cueroseleccionado_ibfk_2` FOREIGN KEY (`idSeleccion`) REFERENCES `seleccioncuero` (`idSeleccion`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.cueroseleccionado: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `cueroseleccionado` DISABLE KEYS */;
/*!40000 ALTER TABLE `cueroseleccionado` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.cuerotrabajar
CREATE TABLE IF NOT EXISTS `cuerotrabajar` (
  `idCueroTrabajar` int(11) NOT NULL AUTO_INCREMENT,
  `idTipoProducto` int(11) NOT NULL,
  `noPartida` int(11) DEFAULT NULL,
  `noPiezas` int(11) DEFAULT NULL,
  `noPiezasActuales` int(11) DEFAULT NULL,
  `descripcion` varchar(30) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idCueroTrabajar`),
  KEY `idTipoProducto` (`idTipoProducto`),
  CONSTRAINT `cuerotrabajar_ibfk_1` FOREIGN KEY (`idTipoProducto`) REFERENCES `tipoproductocuero` (`idTipoProducto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.cuerotrabajar: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `cuerotrabajar` DISABLE KEYS */;
/*!40000 ALTER TABLE `cuerotrabajar` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.invdisptrabajar
CREATE TABLE IF NOT EXISTS `invdisptrabajar` (
  `IdinvDispTrabajar` int(11) NOT NULL AUTO_INCREMENT,
  `idcueroPesado` int(11) NOT NULL,
  `peso` float DEFAULT NULL,
  `pesoActual` float DEFAULT NULL,
  `noPiezas` int(11) DEFAULT NULL,
  `noPiezasActuales` int(11) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`IdinvDispTrabajar`),
  KEY `idcueroPesado` (`idcueroPesado`),
  CONSTRAINT `invdisptrabajar_ibfk_1` FOREIGN KEY (`idcueroPesado`) REFERENCES `cueropesado` (`idcueroPesado`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.invdisptrabajar: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `invdisptrabajar` DISABLE KEYS */;
/*!40000 ALTER TABLE `invdisptrabajar` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.moneda
CREATE TABLE IF NOT EXISTS `moneda` (
  `idMoneda` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(10) DEFAULT NULL,
  `abreviacion` varchar(5) DEFAULT NULL,
  `tipoCambio` float DEFAULT NULL,
  PRIMARY KEY (`idMoneda`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.moneda: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `moneda` DISABLE KEYS */;
/*!40000 ALTER TABLE `moneda` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.pedacera
CREATE TABLE IF NOT EXISTS `pedacera` (
  `idPedacera` int(11) NOT NULL AUTO_INCREMENT,
  `peso` float DEFAULT NULL,
  `pesoActual` float DEFAULT NULL,
  `descripcion` varchar(30) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idPedacera`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.pedacera: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `pedacera` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedacera` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.pedaceratrabajar
CREATE TABLE IF NOT EXISTS `pedaceratrabajar` (
  `idPedacera` int(11) NOT NULL AUTO_INCREMENT,
  `idPedacerafk` int(11) NOT NULL,
  `peso` float DEFAULT NULL,
  `pesoActual` float DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idPedacera`),
  KEY `idPedacerafk` (`idPedacerafk`),
  CONSTRAINT `pedaceratrabajar_ibfk_1` FOREIGN KEY (`idPedacerafk`) REFERENCES `pedacera` (`idPedacera`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.pedaceratrabajar: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `pedaceratrabajar` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedaceratrabajar` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.pedaceraventa
CREATE TABLE IF NOT EXISTS `pedaceraventa` (
  `idPedaceraVenta` int(11) NOT NULL AUTO_INCREMENT,
  `idPedacera` int(11) NOT NULL,
  `peso` float DEFAULT NULL,
  `pesoActual` float DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idPedaceraVenta`),
  KEY `idPedacera` (`idPedacera`),
  CONSTRAINT `pedaceraventa_ibfk_1` FOREIGN KEY (`idPedacera`) REFERENCES `pedaceratrabajar` (`idPedacera`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.pedaceraventa: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `pedaceraventa` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedaceraventa` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.pedido
CREATE TABLE IF NOT EXISTS `pedido` (
  `idPedido` int(11) NOT NULL AUTO_INCREMENT,
  `idCliente` int(11) NOT NULL,
  `idVendedor` int(11) NOT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idPedido`),
  KEY `idCliente` (`idCliente`),
  KEY `idVendedor` (`idVendedor`),
  CONSTRAINT `pedido_ibfk_1` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `pedido_ibfk_2` FOREIGN KEY (`idVendedor`) REFERENCES `vendedor` (`idVendedor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.pedido: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.pedidodetalle
CREATE TABLE IF NOT EXISTS `pedidodetalle` (
  `idPedidoDetalle` int(11) NOT NULL AUTO_INCREMENT,
  `idPedido` int(11) NOT NULL,
  `idCatalogoProducto` int(11) NOT NULL,
  `idCliente` int(11) NOT NULL,
  `cantidad` float DEFAULT NULL,
  `cantidadPendiente` float DEFAULT NULL,
  `unidad` varchar(15) DEFAULT NULL,
  `estatus` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`idPedidoDetalle`),
  KEY `idPedido` (`idPedido`),
  KEY `idCatalogoProducto` (`idCatalogoProducto`),
  KEY `idCliente` (`idCliente`),
  CONSTRAINT `pedidodetalle_ibfk_1` FOREIGN KEY (`idPedido`) REFERENCES `pedido` (`idPedido`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `pedidodetalle_ibfk_2` FOREIGN KEY (`idCatalogoProducto`) REFERENCES `catalogoproducto` (`idCatalogoProducto`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `pedidodetalle_ibfk_3` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.pedidodetalle: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `pedidodetalle` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedidodetalle` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.productosaldo
CREATE TABLE IF NOT EXISTS `productosaldo` (
  `idProductoSaldo` int(11) NOT NULL AUTO_INCREMENT,
  `idTipoProducto` int(11) NOT NULL,
  `idTipoCalibre` int(11) NOT NULL,
  `idSeleccion` int(11) NOT NULL,
  `peso` float DEFAULT NULL,
  `pesoActual` float DEFAULT NULL,
  `noPiezas` int(11) DEFAULT NULL,
  `noPiezasActuales` int(11) DEFAULT NULL,
  `descripcion` varchar(30) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idProductoSaldo`),
  KEY `idTipoProducto` (`idTipoProducto`),
  KEY `idTipoCalibre` (`idTipoCalibre`),
  KEY `idSeleccion` (`idSeleccion`),
  CONSTRAINT `productosaldo_ibfk_1` FOREIGN KEY (`idTipoProducto`) REFERENCES `tipoproductocuero` (`idTipoProducto`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `productosaldo_ibfk_2` FOREIGN KEY (`idTipoCalibre`) REFERENCES `tipocalibre` (`idTipoCalibre`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `productosaldo_ibfk_3` FOREIGN KEY (`idSeleccion`) REFERENCES `seleccioncuero` (`idSeleccion`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.productosaldo: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `productosaldo` DISABLE KEYS */;
/*!40000 ALTER TABLE `productosaldo` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.saldoterminadotrabajar
CREATE TABLE IF NOT EXISTS `saldoterminadotrabajar` (
  `idSaldoTerminadoTrabajar` int(11) NOT NULL AUTO_INCREMENT,
  `idProductoSaldo` int(11) NOT NULL,
  `peso` float DEFAULT NULL,
  `pesoActual` float DEFAULT NULL,
  `noPiezas` int(11) DEFAULT NULL,
  `noPiezasActuales` int(11) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idSaldoTerminadoTrabajar`),
  KEY `idProductoSaldo` (`idProductoSaldo`),
  CONSTRAINT `saldoterminadotrabajar_ibfk_1` FOREIGN KEY (`idProductoSaldo`) REFERENCES `productosaldo` (`idProductoSaldo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.saldoterminadotrabajar: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `saldoterminadotrabajar` DISABLE KEYS */;
/*!40000 ALTER TABLE `saldoterminadotrabajar` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.saldoventa
CREATE TABLE IF NOT EXISTS `saldoventa` (
  `idSaldoVenta` int(11) NOT NULL AUTO_INCREMENT,
  `idSaldoTerminadoTrabajar` int(11) NOT NULL,
  `peso` float DEFAULT NULL,
  `pesoActual` float DEFAULT NULL,
  `noPiezas` int(11) DEFAULT NULL,
  `noPiezasActuales` int(11) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idSaldoVenta`),
  KEY `idSaldoTerminadoTrabajar` (`idSaldoTerminadoTrabajar`),
  CONSTRAINT `saldoventa_ibfk_1` FOREIGN KEY (`idSaldoTerminadoTrabajar`) REFERENCES `saldoterminadotrabajar` (`idSaldoTerminadoTrabajar`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.saldoventa: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `saldoventa` DISABLE KEYS */;
/*!40000 ALTER TABLE `saldoventa` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.seleccioncuero
CREATE TABLE IF NOT EXISTS `seleccioncuero` (
  `idSeleccion` int(11) NOT NULL AUTO_INCREMENT,
  `seleccion` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`idSeleccion`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.seleccioncuero: ~6 rows (aproximadamente)
/*!40000 ALTER TABLE `seleccioncuero` DISABLE KEYS */;
INSERT INTO `seleccioncuero` (`idSeleccion`, `seleccion`) VALUES
	(1, 'Estandar'),
	(2, 'B'),
	(3, 'C'),
	(4, 'D'),
	(5, 'E'),
	(6, 'F');
/*!40000 ALTER TABLE `seleccioncuero` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.terminadoventa
CREATE TABLE IF NOT EXISTS `terminadoventa` (
  `idTerminadoVenta` int(11) NOT NULL AUTO_INCREMENT,
  `idInvDispTrabajar` int(11) NOT NULL,
  `peso` float DEFAULT NULL,
  `pesoActual` float DEFAULT NULL,
  `noPiezas` int(11) DEFAULT NULL,
  `noPiezasActuales` int(11) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idTerminadoVenta`),
  KEY `idInvDispTrabajar` (`idInvDispTrabajar`),
  CONSTRAINT `terminadoventa_ibfk_1` FOREIGN KEY (`idInvDispTrabajar`) REFERENCES `invdisptrabajar` (`IdinvDispTrabajar`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.terminadoventa: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `terminadoventa` DISABLE KEYS */;
/*!40000 ALTER TABLE `terminadoventa` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.tipocalibre
CREATE TABLE IF NOT EXISTS `tipocalibre` (
  `idTipoCalibre` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`idTipoCalibre`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.tipocalibre: ~13 rows (aproximadamente)
/*!40000 ALTER TABLE `tipocalibre` DISABLE KEYS */;
INSERT INTO `tipocalibre` (`idTipoCalibre`, `descripcion`) VALUES
	(1, '4-5'),
	(2, '5-6'),
	(3, '6-7'),
	(4, '7-8'),
	(5, '8-9'),
	(6, '9-10'),
	(7, '10-11'),
	(8, '40-45'),
	(9, '45-50'),
	(10, '50-55'),
	(11, '55-60'),
	(12, '60-65'),
	(13, '65-70');
/*!40000 ALTER TABLE `tipocalibre` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.tipoproductocuero
CREATE TABLE IF NOT EXISTS `tipoproductocuero` (
  `idTipoProducto` int(11) NOT NULL AUTO_INCREMENT,
  `tipoProducto` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`idTipoProducto`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.tipoproductocuero: ~6 rows (aproximadamente)
/*!40000 ALTER TABLE `tipoproductocuero` DISABLE KEYS */;
INSERT INTO `tipoproductocuero` (`idTipoProducto`, `tipoProducto`) VALUES
	(1, 'Crupon Sillero'),
	(2, 'Centro Quebracho'),
	(3, 'Centro Castaño'),
	(4, 'Delantero Sillero'),
	(5, 'Delantero Doble'),
	(6, 'Delantero Suela');
/*!40000 ALTER TABLE `tipoproductocuero` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.usuario
CREATE TABLE IF NOT EXISTS `usuario` (
  `idUsuario` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(15) DEFAULT NULL,
  `contrasenia` varchar(15) DEFAULT NULL,
  `nombre` varchar(30) DEFAULT NULL,
  `tipo` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.usuario: ~3 rows (aproximadamente)
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`idUsuario`, `usuario`, `contrasenia`, `nombre`, `tipo`) VALUES
	(1, 'mingo', '123', 'Domingo Luna', 'Sistemas'),
	(2, 'semiterminado', '123', 'Semiterminado', 'Semiterminado'),
	(3, 'fernando', 'f.robless', 'Fernando Robles', 'Produccion');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.vendedor
CREATE TABLE IF NOT EXISTS `vendedor` (
  `idVendedor` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(40) DEFAULT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `correo` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`idVendedor`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.vendedor: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `vendedor` DISABLE KEYS */;
/*!40000 ALTER TABLE `vendedor` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.venta
CREATE TABLE IF NOT EXISTS `venta` (
  `idVenta` int(11) NOT NULL AUTO_INCREMENT,
  `idCliente` int(11) NOT NULL,
  `idVendedor` int(11) NOT NULL,
  `descripcion` varchar(80) DEFAULT NULL,
  `total` float DEFAULT NULL,
  `totalNacional` float DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idVenta`),
  KEY `idCliente` (`idCliente`),
  KEY `idVendedor` (`idVendedor`),
  CONSTRAINT `venta_ibfk_1` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `venta_ibfk_2` FOREIGN KEY (`idVendedor`) REFERENCES `vendedor` (`idVendedor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.venta: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `venta` DISABLE KEYS */;
/*!40000 ALTER TABLE `venta` ENABLE KEYS */;

-- Volcando estructura para tabla esmar_t.ventadetalle
CREATE TABLE IF NOT EXISTS `ventadetalle` (
  `idVentaDetalle` int(11) NOT NULL AUTO_INCREMENT,
  `idVenta` int(11) NOT NULL,
  `idPedido` int(11) NOT NULL,
  `idMoneda` int(11) NOT NULL,
  `cantidad` float DEFAULT NULL,
  `precio` float DEFAULT NULL,
  `precioNacional` float DEFAULT NULL,
  PRIMARY KEY (`idVentaDetalle`),
  KEY `idVenta` (`idVenta`),
  KEY `idPedido` (`idPedido`),
  KEY `idMoneda` (`idMoneda`),
  CONSTRAINT `ventadetalle_ibfk_1` FOREIGN KEY (`idVenta`) REFERENCES `venta` (`idVenta`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ventadetalle_ibfk_2` FOREIGN KEY (`idPedido`) REFERENCES `pedido` (`idPedido`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ventadetalle_ibfk_3` FOREIGN KEY (`idMoneda`) REFERENCES `moneda` (`idMoneda`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla esmar_t.ventadetalle: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `ventadetalle` DISABLE KEYS */;
/*!40000 ALTER TABLE `ventadetalle` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
