use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actProv')
begin 
  drop
    procedure sp_actProv
end
go

create procedure sp_actProv
  (
    @nombreProveedor varchar(100)
    , @estatus       int
    , @idProveedor   int
  )
  as begin
  
    update
      tb_proveedor 
      
    set
      nombreProveedor = @nombreProveedor
      , estatus = @estatus
      
    where
      idProveedor = @idProveedor
  end
go