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
  , @kg             float
  , @decimetros     float
  , @pies           float
  , @bandera        int
)
as begin

  if @bandera = 1
  begin
    update
      tb_invTerminadoPesado
      
    set
      noPiezasActuales = noPiezasActuales-@piezasUtilizar
      , kgTotalesActual = kgTotalesActual-@kg
      , decimetrosActual = decimetrosActual-@decimetros
      , piesActual = piesActual-@pies
      
    where
      idInvTerminadoPesado = @idInvTerminado
  end
  
  else if @bandera = 0
  begin 
    update
      tb_invTerminado
      
    set
      noPiezasActuales = noPiezasActuales-@piezasUtilizar
      , kgTotalesActual = kgTotalesActual-@kg
      , decimetrosActual = decimetrosActual-@decimetros
      , piesActual = piesActual-@pies
      
    where
      idInvTerminado = @idInvTerminado
  end
  
  if @bandera = 2
  begin
    update
      tb_invTerminadoManual
      
    set
      noPiezasActuales = noPiezasActuales-@piezasUtilizar
      , kgTotalesActual = kgTotalesActual-@kg
      , decimetrosActual = decimetrosActual-@decimetros
      , piesActual = piesActual-@pies
      
    where
      idInvTerminadoManual = @idInvTerminado
  end
  
end
go