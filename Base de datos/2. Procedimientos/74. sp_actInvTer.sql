use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actInvTer')
begin 
  drop
    procedure sp_actInvTer
end
go

create procedure sp_actInvTer
(
  @idInvTerminado   int
  , @piezasUtilizar int
)
as begin

  update
    tb_invTerminado
    
  set
    noPiezasActuales = noPiezasActuales-@piezasUtilizar
    
  where
    idInvTerminado = @idInvTerminado
end
go