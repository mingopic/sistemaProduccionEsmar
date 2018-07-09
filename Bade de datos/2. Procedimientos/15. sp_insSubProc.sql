use esmarProd
go

create procedure sp_insSubProc
  (
    @subProceso varchar(20)
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