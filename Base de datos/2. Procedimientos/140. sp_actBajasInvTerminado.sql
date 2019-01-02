use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actBajasInvTerminado')
begin 
  drop
    procedure sp_actBajasInvTerminado
end
go

create procedure sp_actBajasInvTerminado
(
  @piezasUtilizar        int
  , @idInvTerminado      int
  , @kgDescontar         float
  , @piesDescontar       float
  , @decimetrosDescontar float
  , @bandera             float
)
as begin
  
  if @bandera = 0
  begin
  
  update
      tb_invTerminado
      
    set
      noPiezasActuales = noPiezasActuales-@piezasUtilizar
      , kgTotalesActual = kgTotalesActual-@kgDescontar
      , piesActual = piesActual-@piesDescontar
      , decimetrosActual = decimetrosActual-@decimetrosDescontar
      
    where
      idInvTerminado = @idInvTerminado
      
  end
  
  else if @bandera = 1
  begin
    update
      tb_invTerminadoPesado
      
    set
      noPiezasActuales = noPiezasActuales-@piezasUtilizar
      , kgTotalesActual = kgTotalesActual-@kgDescontar
      , piesActual = piesActual-@piesDescontar
      , decimetrosActual = decimetrosActual-@decimetrosDescontar
      
    where
      idInvTerminadoPesado = @idInvTerminado
  end
  
  else if @bandera = 2
  begin
    update
        tb_invTerminadoManual
        
      set
        noPiezasActuales = noPiezasActuales-@piezasUtilizar
        , kgTotalesActual = kgTotalesActual-@kgDescontar
        , piesActual = piesActual-@piesDescontar
        , decimetrosActual = decimetrosActual-@decimetrosDescontar
        
      where
        idInvTerminadoManual = @idInvTerminado
  end
  
end
go