use esmarProd
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