use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtProvAct')
begin 
  drop
    procedure sp_obtProvAct
end
go

create procedure sp_obtProvAct
  as begin
  
    select 
      idProveedor
      , nombreProveedor 
      
    from 
      tb_proveedor 
      
    where 
      estatus = 1
  end
go