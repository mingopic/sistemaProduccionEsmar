use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actSubProc')
begin 
  drop
    procedure sp_actSubProc
end
go

create procedure sp_actSubProc
  (
    @subProceso     varchar(50)
    , @proceso      varchar(20)
    , @idSubProceso int
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
    
    update
      tb_subProceso
      
    set
      idProceso = @idProceso
      , descripcion = @subProceso
      
    where
      idSubProceso = @idSubProceso
  end
go