use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtSubProc')
begin 
  drop
    procedure sp_obtSubProc
end
go

create procedure sp_obtSubProc
  (
    @nombreproceso varchar(20)
  )
  as begin
  
    select
      sp.idSubProceso
      , sp.descripcion as 'subProceso'
      , p.descripcion as 'proceso'
      
    from 
      tb_subProceso as sp 
      
      inner join 
        tb_proceso as p
      on 
        sp.idProceso = p.idProceso
        
    where
      p.descripcion like @nombreproceso 
      
    order by
      sp.idProceso
  end
go