use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actInvCrossSemi')
begin 
  drop
    procedure sp_actInvCrossSemi
end
go

create procedure sp_actInvCrossSemi
(
  @idInvCrossSemi      int
  , @piezasUtilizar    int
)
as begin

  update
    tb_invCrossSemi
    
  set
    noPiezasActuales = noPiezasActuales-@piezasUtilizar
    
  where
    idInvCrossSemi = @idInvCrossSemi
end
go