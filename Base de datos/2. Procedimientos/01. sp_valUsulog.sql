use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_valUsulog')
begin 
  drop
    procedure sp_valUsulog
end
go

create procedure sp_valUsulog 
  (
    @usuario varchar(15)
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