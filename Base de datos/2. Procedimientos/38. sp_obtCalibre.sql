use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtCalibre')
begin 
  drop
    procedure sp_obtCalibre
end
go

create procedure sp_obtCalibre
(
  @accion int = 0
)
as begin
  
  if @accion = 1
  begin
  
    select
      *
      
    from
      tb_calibre
  end  
  
  else begin
    
    select
      *
      
    from
      tb_calibre
    
    where
      estatus = 1
  end
end
go