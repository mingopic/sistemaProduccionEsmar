use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtBajasPartidaDet')
begin 
  drop
    procedure sp_obtBajasPartidaDet
end
go

create procedure sp_obtBajasPartidaDet
as begin
    
  select
    p.nombreProveedor
    , rc.noCamion
    , tc.descripcion as tipoCuero
    , tr.descripcion as tipoRecorte
    , bpd.noPiezas
    , bpd.motivo
    , bpd.fechaBaja
  
  from
    tb_bajasPartidaDet as bpd
  inner join
    tb_partidaDet as pd
  on
    pd.idPartidaDet = bpd.idPartidaDet
    
  inner join
    tb_recepcionCuero as rc
  on
    rc.idRecepcionCuero = pd.idRecepcionCuero
    
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
    tr.idTipoRecorte = pd.idTipoRecorte
end
go