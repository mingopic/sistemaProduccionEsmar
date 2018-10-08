use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actBajasInvTerminado')
begin 
  drop
    procedure sp_actBajasInvTerminado
end
go

create procedure sp_actBajasInvTerminado
(
  @piezasUtilizar        int
  , @idInvTerminado      int
  , @kgDescontar         float
  , @piesDescontar       float
  , @decimetrosDescontar float
  
)
as begin
    
  update
    tb_invTerminado
    
  set
    noPiezasActuales = noPiezasActuales-@piezasUtilizar
    , kgTotalesActual = kgTotalesActual-@kgDescontar
    , piesActual = piesActual-@piesDescontar
    , decimetrosActual = decimetrosActual-@decimetrosDescontar
    
  where
    idInvTerminado = @idInvTerminado
end
go