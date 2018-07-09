use esmarProd
go

create procedure sp_insProv
  (
    @nombreProveedor varchar(100)
    , @estatus       int
  )
  as begin
  
    insert into
      tb_proveedor 
      
    values
      (
        @nombreProveedor
        , @estatus
      )
  end
go