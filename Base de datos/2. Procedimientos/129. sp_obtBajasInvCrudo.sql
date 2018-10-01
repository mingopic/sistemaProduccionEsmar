use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtBajasInvCrudo')
begin 
  drop
    procedure sp_obtBajasInvCrudo
end
go

create procedure sp_obtBajasInvCrudo
as begin
    
  select
    p.nombreProveedor
    , rc.noCamion
    , tc.descripcion as tipoCuero
    , tr.descripcion as tipoRecorte
    , bic.noPiezas
    , bic.motivo
    , bic.fechaBaja
  
  from
    tb_bajasInventarioCrudo as bic
  inner join
    tb_inventarioCrudo as ic
  on
    ic.idInventarioCrudo = bic.idInventarioCrudo
    
  inner join
    tb_recepcionCuero as rc
  on
    rc.idRecepcionCuero = ic.idRecepcionCuero
    
  inner join
    tb_proveedor as p
  on
    p.idProveedor = rc.idProveedor
    
  inner join
    tb_tipoCuero as tc
  on
    tc.idTipoCuero = rc.idTipoCuero
  
  inner join
    tb_tipoRecorte as tr
  on
    tr.idTipoRecorte = ic.idTipoRecorte
end
go