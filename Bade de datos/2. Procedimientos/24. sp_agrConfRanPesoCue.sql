use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrConfRanPesoCue')
begin 
  drop
    procedure sp_agrConfRanPesoCue
end
go

create procedure sp_agrConfRanPesoCue
(
  @rangoMin   int
  , @rangoMax int
)
as begin

  declare @fechaConfig datetime
  
  set @fechaConfig =
    (
      select
        getdate()
    )
  
  insert into
    tb_rangoPesoCuero
    
  values
    (
      @rangoMin
      , @rangoMax
      , @fechaConfig
    )
end
go