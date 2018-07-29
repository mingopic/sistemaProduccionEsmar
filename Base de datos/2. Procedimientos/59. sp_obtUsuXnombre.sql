use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtUsuXnombre')
begin 
  drop
    procedure sp_obtUsuXnombre
end
go

create procedure sp_obtUsuXnombre
  (
    @usuario varchar(100)
  )
  as begin
    select 
      *
      
    from
      tb_usuario
      
    where
      usuario = @usuario
  end
go