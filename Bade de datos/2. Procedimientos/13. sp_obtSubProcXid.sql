use esmarProd
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