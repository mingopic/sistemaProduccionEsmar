use esmarProd
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