use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actBajasInvSemiterminado')
begin 
  drop
    procedure sp_actBajasInvSemiterminado
end
go

create procedure sp_actBajasInvSemiterminado
(
  @piezasUtilizar       int
  , @idInvSemiterminado int
  , @kgDescontar        float
  
)
as begin
    
  update
    tb_invSemiterminado
    
  set
    noPiezasActuales = noPiezasActuales-@piezasUtilizar
    , kgTotalesActuales = kgTotalesActuales-@kgDescontar
    
  where
    idInvSemiterminado = @idInvSemiterminado
end
go