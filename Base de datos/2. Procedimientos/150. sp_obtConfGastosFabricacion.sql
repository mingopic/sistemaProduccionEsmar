use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtConfGastosFabricacion')
begin 
  drop procedure sp_obtConfGastosFabricacion
end
go

create procedure sp_obtConfGastosFabricacion
as begin
  declare @fecha datetime
  
  set @fecha =
  (
    select max
      (fecha) as fecha
    from
      tb_confGastosFabricacion
  )
  
  select
    cg.idConfGastosFabricacion
    , cg.idTipoRecorte
    , tr.descripcion as descTipoRecorte
    , [costo] = cg.costo
    
  from 
    tb_confGastosFabricacion cg
    
  inner join 
    tb_tipoRecorte tr
  on
    tr.idTipoRecorte = cg.idTipoRecorte
  where
    cg.fecha = @fecha
end