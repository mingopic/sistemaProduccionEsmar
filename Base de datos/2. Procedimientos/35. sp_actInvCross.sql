use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actInvCross')
begin 
  drop
    procedure sp_actInvCross
end
go

create procedure sp_actInvCross
(
  @idInvPCross      int
  , @piezasUtilizar int
  , @kgDescontar    float
)
as begin
  
  declare
    @idPartidaDet int
  
  update
    tb_invCross
    
  set
    noPiezasActuales = noPiezasActuales-@piezasUtilizar
    , kgActual = kgActual-@kgDescontar
    
  where
    idInvPCross = @idInvPCross
end
go
