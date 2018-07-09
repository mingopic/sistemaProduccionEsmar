use esmarProd
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