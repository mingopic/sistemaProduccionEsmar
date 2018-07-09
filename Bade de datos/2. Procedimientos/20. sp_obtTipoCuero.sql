use esmarProd
go

create procedure sp_obtTipoCuero
  as begin
  
    select 
      idTipoCuero
      , descripcion 
      
    from 
      tb_tipoCuero
  end
go