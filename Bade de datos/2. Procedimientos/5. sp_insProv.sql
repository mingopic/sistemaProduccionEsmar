use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insProv')
begin 
  drop
    procedure sp_insProv
end
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