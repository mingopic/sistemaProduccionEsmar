use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrFormSubProc')
begin 
  drop
    procedure sp_agrFormSubProc
end
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