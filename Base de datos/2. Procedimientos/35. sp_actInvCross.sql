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
)
as begin

  update
    tb_invCross
    
  set
    noPiezasActuales = noPiezasActuales-@piezasUtilizar
    
  where
    idInvPCross = @idInvPCross
end
go