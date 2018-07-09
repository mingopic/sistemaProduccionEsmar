use esmarProd
go

create procedure sp_obtProv
  as begin
  
    select 
      nombreProveedor
      , estatus
      
    from
      tb_proveedor
  end
go