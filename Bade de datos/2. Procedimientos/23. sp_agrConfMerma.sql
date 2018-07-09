use esmarProd
go

create procedure sp_agrConfMerma
(
  @idTipoMerma    int
  ,@porcMermaAcep float
)
as begin

  declare @fechaConfig datetime
  
  set @fechaConfig =
    (
      select
        getdate()
    )
  
  insert into
    tb_configMerma
    
  values
    (
      @idTipoMerma
      , @porcMermaAcep
      ,@fechaConfig
    )
end
go