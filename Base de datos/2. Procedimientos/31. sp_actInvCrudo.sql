use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actInvCrudo')
begin 
  drop
    procedure sp_actInvCrudo
end
go

create procedure sp_actInvCrudo
(
  @piezasUtilizar    int
  , @kgDescontar	     float
  , @idInventarioCrudo int
)
as begin
    
  update
    tb_inventarioCrudo
    
  set
    noPiezasActual = noPiezasActual-@piezasUtilizar
    , kgTotalActual = kgTotalActual-@kgDescontar
    
  where
    idInventarioCrudo = @idInventarioCrudo
end
go