use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actBajasInvCross')
begin 
  drop
    procedure sp_actBajasInvCross
end
go

create procedure sp_actBajasInvCross
(
  @piezasUtilizar      int
  , @idInvPCross       int
  , @kgDescontar       float
  
)
as begin
    
  update
    tb_invCross
    
  set
    noPiezasActuales = noPiezasActuales-@piezasUtilizar
    , kgActual = kgActual-@kgDescontar
    
  where
    idInvPCross = @idInvPCross
end
go