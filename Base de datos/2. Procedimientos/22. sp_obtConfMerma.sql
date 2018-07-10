use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtConfMerma')
begin 
  drop
    procedure sp_obtConfMerma
end
go

create procedure sp_obtConfMerma
as begin
  declare @salAcep     float
  declare @humedadAcep float
  declare @cacheteAcep float
  declare @tarimasAcep float
  
  set @salAcep =
  (
    select
      porcMermaAcep
      
    from
      tb_configMerma
      
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
          
        from
          tb_configMerma
          
        where
          idTipoMerma = 1
      )
  )
  
  set @humedadAcep =
  (
    select
      porcMermaAcep
      
    from
      tb_configMerma
      
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
          
        from
          tb_configMerma
          
        where
          idTipoMerma = 2
      )
  )
  
  set @cacheteAcep =
  (
    select
      porcMermaAcep
      
    from
      tb_configMerma
      
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
          
        from
          tb_configMerma
          
        where
          idTipoMerma = 3
      )
  )
  
  set @tarimasAcep =
  (
    select
      porcMermaAcep
      
    from
      tb_configMerma
      
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
          
        from
          tb_configMerma
          
        where
          idTipoMerma = 4
      )
  )
  
  select
    @salAcep as salAcep
    , @humedadAcep as humedadAcep
    , @cacheteAcep as cacheteAcep
    , @tarimasAcep as tarimasAcep
end
go