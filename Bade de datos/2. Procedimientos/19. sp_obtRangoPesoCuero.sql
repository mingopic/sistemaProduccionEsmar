use esmarProd
go

create procedure sp_obtRangoPesoCuero 
as begin

  select
    idRangoPesoCuero
    ,rangoMin
    ,rangoMax
    
  from
    tb_rangoPesoCuero
    
  where
    fechaConfig = 
    (
      select 
        max(fechaConfig)
        
      from
        tb_rangoPesoCuero
    )
end
go