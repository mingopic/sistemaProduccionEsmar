use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtSubProcXid')
begin 
  drop
    procedure sp_obtSubProcXid
end
go

create procedure sp_obtSubProcXid
  (
    @idProceso int
  )
  as begin
  
    select
      *
      
    from 
      tb_subProceso
      
    where
      idProceso = @idProceso 
  end
go