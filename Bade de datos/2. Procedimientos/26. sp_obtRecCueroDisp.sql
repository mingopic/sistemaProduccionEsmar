use esmarProd
go

create procedure sp_obtRecCueroDisp
as begin

  select
    p.nombreProveedor
    , rc.noCamion
    , tp.descripcion
    , ic.noPiezasActual
    , rc.kgTotal
    , rc.kgTotal/ic.noPiezasActual as PromKgPieza
    , rc.fechaEntrada
    , rc.idRecepcionCuero
    
  from
    tb_proveedor as p
    
    inner join
      tb_recepcionCuero as rc
    on
      p.idProveedor = rc.idProveedor
      
    inner join
      tb_inventarioCrudo as ic
    on
      ic.idRecepcionCuero = rc.idRecepcionCuero
      
    inner join
      tb_tipoCuero as tp
    on
      rc.idTipoCuero = tp.idTipoCuero
      
  where
    ic.noPiezasActual > 0
end
go