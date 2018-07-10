use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtRangoPesoCuero')
begin 
  drop
    procedure sp_obtRangoPesoCuero
end
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