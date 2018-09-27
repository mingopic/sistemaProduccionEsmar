use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtRecCueroDisp')
begin 
  drop
    procedure sp_obtRecCueroDisp
end
go

create procedure sp_obtRecCueroDisp
as begin 
  
  select
    p.nombreProveedor
    , rc.noCamion
    , tp.descripcion
    , case
        when tr.descripcion = 'Entero' then 'Entero'
        when tr.descripcion = 'Delantero Sillero' then 'Delantero'
        when tr.descripcion = 'Crupon Sillero' then 'Crupon'
        when tr.descripcion = 'Lados' then 'Lados'
        when tr.descripcion = 'Centro' then 'Centro'
        when tr.descripcion = 'Delantero Suela' then 'Delantero Suela'
      end as recorte
    , ic.noPiezasActual
    , ic.kgTotalActual
    , ic.kgTotalActual/ic.noPiezasActual as PromKgPieza
    , rc.fechaEntrada
    , rc.idRecepcionCuero
    , ic.idInventarioCrudo
    
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
    
    inner join
      tb_tipoRecorte tr
    on
      tr.idTipoRecorte = ic.idTipoRecorte
      
  where
    ic.noPiezasActual > 0
end
go