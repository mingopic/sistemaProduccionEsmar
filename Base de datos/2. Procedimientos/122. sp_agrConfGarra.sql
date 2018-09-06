use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrConfGarra')
begin 
  drop
    procedure sp_agrConfGarra
end
go

create procedure sp_agrConfGarra
(
  @costo float
)
as begin

  declare @fechaConfig datetime
  
  set @fechaConfig =
    (
      select
        getdate()
    )
  
  insert into
    tb_costoGarra
    
  values
    (
      @costo
      ,@fechaConfig
    )
end
go