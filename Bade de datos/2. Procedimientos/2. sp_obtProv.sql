use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtProv')
begin 
  drop
    procedure sp_obtProv
end
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