use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtConfPrecioCuero')
begin 
  drop procedure sp_obtConfPrecioCuero
end
go

create procedure sp_obtConfPrecioCuero
as begin
  
  select
    cp.idConfPrecioCuero
    , cp.idTipoRecorte
    , tr.descripcion as descTipoRecorte
    , [porcentaje] = cp.porcentaje * 100
    
  from 
    tb_confPrecioCuero cp
    
    inner join 
      tb_tipoRecorte tr
    on
      tr.idTipoRecorte = cp.idTipoRecorte
end