use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtConfPrecioManoDeObra')
begin 
  drop procedure sp_obtConfPrecioManoDeObra
end
go

create procedure sp_obtConfPrecioManoDeObra
as begin
  declare @fecha datetime
  
  set @fecha =
  (
    select max
      (fecha) as fecha
    from
      tb_confPrecioManoDeObra
  )
  
  select
    cp.idConfPrecioManoDeObra
    , cp.idTipoRecorte
    , tr.descripcion as descTipoRecorte
    , [costo] = cp.costo
    
  from 
    tb_confPrecioManoDeObra cp
    
  inner join 
    tb_tipoRecorte tr
  on
    tr.idTipoRecorte = cp.idTipoRecorte
  where
    cp.fecha = @fecha
end