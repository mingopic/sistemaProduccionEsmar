use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtConfGarra')
begin 
  drop
    procedure sp_obtConfGarra
end
go

create procedure sp_obtConfGarra
as begin
  declare @costo     float
  
  set @costo =
  (
    select
      costo
      
    from
      tb_costoGarra
      
    where
      idCostoGarra =
      (
        select
          max(idCostoGarra)
          
        from
          tb_costoGarra
      )
  )
  
  select
    @costo as costo
end
go