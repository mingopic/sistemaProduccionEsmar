use esmarProd
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