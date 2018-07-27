use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actInvSemiterminado')
begin 
  drop
    procedure sp_actInvSemiterminado
end
go

create procedure sp_actInvSemiterminado
(
  @idInvSemiterminado	int
  , @piezasUtilizar 	int
)
as begin

  update
    tb_invSemiterminado
    
  set
    noPiezasActuales = noPiezasActuales-@piezasUtilizar
    
  where
    idInvSemiterminado = @idInvSemiterminado
end
go