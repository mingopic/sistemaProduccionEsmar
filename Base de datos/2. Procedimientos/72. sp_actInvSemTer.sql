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
  @idInvSemTer      int
  , @piezasUtilizar int
  , @bandera        int
)
as begin
  
  if @bandera = 0
  begin
  
    update
      tb_invSemTer
      
    set
      noPiezasActuales = noPiezasActuales-@piezasUtilizar
      
    where
      idInvSemTer = @idInvSemTer
  
  end
  
  else
  begin
  
    update
      tb_invSemTerPesado
      
    set
      noPiezasActuales = noPiezasActuales-@piezasUtilizar
      
    where
      idInvSemTerPesado = @idInvSemTer
  
  end
  
end
go