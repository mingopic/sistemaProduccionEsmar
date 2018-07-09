use esmarProd
go

create procedure sp_obtListaPartida
as begin

  select
    rc.noCamion
    , p.nombreproveedor
    , ic.noPiezasActual
    , rc.kgTotal
    
  from
    tb_proveedor as p
    
    inner join
      tb_recepcionCuero as rc
    on
      p.idProveedor = rc.idProveedor
    inner join
      tb_inventarioCrudo as ic
    on
      rc.idRecepcionCuero = ic.idRecepcionCuero
end
go