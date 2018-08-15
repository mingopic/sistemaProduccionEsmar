use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insSubProc')
begin 
  drop
    procedure sp_insSubProc
end
go

create procedure sp_insSubProc
  (
    @subProceso varchar(50)
    , @proceso  varchar(20)
  )
  as begin
  
    declare @idProceso int
    
    set @idProceso = 
    (
      select
        idProceso
        
      from 
        tb_proceso
        
      where 
        descripcion = @proceso
    )
    
    insert into 
      tb_subProceso
      
    values
      (
        @idProceso
        ,@subProceso
      )
  end
go