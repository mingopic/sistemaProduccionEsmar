use esmarProd
go

create procedure sp_obtSubProcXdesc
  (
    @subProceso varchar(20)
  )
  as begin
  
    select
      *
      
    from 
      tb_subProceso
      
    where 
      descripcion = @subProceso
  end
go