use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtSubProcesos')
begin 
  drop
    procedure sp_obtSubProcesos
end
go

create procedure sp_obtSubProcesos
  as begin
  
    select 
      idSubproceso
      , descripcion
    from 
      tb_subProceso
  end
go