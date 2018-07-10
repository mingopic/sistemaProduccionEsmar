use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtTipoCuero')
begin 
  drop
    procedure sp_obtTipoCuero
end
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