use esmarProd
go
 
create procedure sp_agrFormSubProc 
  (
    @idSubProceso int
  )
  as begin
  
    declare @fechaCreacion datetime
    
    set @fechaCreacion = 
    (
      select getdate()
    )
    
    insert into 
      tb_formXsubProc 
      
    values 
      (
        @idSubproceso
        , @fechaCreacion
      )
  end
go