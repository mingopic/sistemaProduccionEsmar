use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actInvSemTer')
begin 
  drop
    procedure sp_actInvSemTer
end
go

create procedure sp_actInvSemTer
(
  @idInvSemTer   int
  , @piezasUtilizar int
)
as begin

  update
    tb_invSemTer
    
  set
    noPiezasActuales = noPiezasActuales-@piezasUtilizar
    
  where
    idInvSemTer = @idInvSemTer
end
go