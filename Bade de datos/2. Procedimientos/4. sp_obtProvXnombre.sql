use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtProvXnombre')
begin 
  drop
    procedure sp_obtProvXnombre
end
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