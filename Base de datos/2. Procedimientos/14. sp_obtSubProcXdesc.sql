use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtSubProcXdesc')
begin 
  drop
    procedure sp_obtSubProcXdesc
end
go

create procedure sp_obtSubProcXdesc
  (
    @subProceso varchar(50)
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