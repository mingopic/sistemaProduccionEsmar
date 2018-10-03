use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actBajasPartidaDet')
begin 
  drop
    procedure sp_actBajasPartidaDet
end
go

create procedure sp_actBajasPartidaDet
(
  @piezasUtilizar      int
  , @idPartidaDet      int
  
)
as begin
    
  update
    tb_partidaDet
    
  set
    noPiezasAct = noPiezasAct-@piezasUtilizar
    
  where
    idPartidaDet = @idPartidaDet
end
go