use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actBajasInvCrudo')
begin 
  drop
    procedure sp_actBajasInvCrudo
end
go

create procedure sp_actBajasInvCrudo
(
  @piezasUtilizar      int
  , @idInventarioCrudo int
)
as begin
    
  update
    tb_inventarioCrudo
    
  set
    noPiezasActual = noPiezasActual-@piezasUtilizar
    
  where
    idInventarioCrudo = @idInventarioCrudo
end
go