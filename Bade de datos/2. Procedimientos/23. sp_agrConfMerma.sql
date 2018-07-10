use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrConfMerma')
begin 
  drop
    procedure sp_agrConfMerma
end
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