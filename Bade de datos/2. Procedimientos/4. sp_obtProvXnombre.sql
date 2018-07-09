use esmarProd
go

create procedure sp_obtProvXnombre
  (
    @nombreProveedor varchar(100)
  )
  as begin
    select 
      *
      
    from
      tb_proveedor
      
    where
      nombreProveedor = @nombreProveedor
  end
go