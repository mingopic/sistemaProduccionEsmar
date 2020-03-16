use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrInvTer')
begin 
  drop
    procedure sp_agrInvTer
end
go

create procedure sp_agrInvTer
(
	@idInvSemTer    int
  , @bandera      int
	, @idCalibre    int
	, @idSeleccion  int
	, @noPiezas     int
	, @kgTotales    float
  , @decimetros   float
  , @pies         float
)
as begin

  if @bandera = 1
  begin
  
    insert into
      tb_invTerminadoPesado
      (
        idInvSemTerPesado
        , idCalibre
        , idSeleccion
        , noPiezas
        , noPiezasActuales
        , kgTotales
        , kgTotalesActual
        , decimetros
        , decimetrosActual
        , pies
        , piesActual
        , fechaEntrada
      )
      
    values
      (
        @idInvSemTer
        , @idCalibre
        , @idSeleccion
        , @noPiezas
        , @noPiezas
        , @kgTotales
        , @kgTotales
        , @decimetros
        , @decimetros
        , @pies
        , @pies
        , getdate()
      )
    
  end
  
  else if @bandera = 0
  begin
  
    insert into
      tb_invTerminado
      (
        idInvSemTer
        , idCalibre
        , idSeleccion
        , noPiezas
        , noPiezasActuales
        , kgTotales
        , kgTotalesActual
        , decimetros
        , decimetrosActual
        , pies
        , piesActual
        , fechaEntrada
      )
      
    values
      (
        @idInvSemTer
        , @idCalibre
        , @idSeleccion
        , @noPiezas
        , @noPiezas
        , @kgTotales
        , @kgTotales
        , @decimetros
        , @decimetros
        , @pies
        , @pies
        , getdate()
      )
    
  end
	
  else if @bandera = 2
  begin
  
    insert into
      tb_invTerminadoManual
      (
        idInvSemTerManual
        , idCalibre
        , idSeleccion
        , noPiezas
        , noPiezasActuales
        , kgTotales
        , kgTotalesActual
        , decimetros
        , decimetrosActual
        , pies
        , piesActual
        , fechaEntrada
      )
      
    values
      (
        @idInvSemTer
        , @idCalibre
        , @idSeleccion
        , @noPiezas
        , @noPiezas
        , @kgTotales
        , @kgTotales
        , @decimetros
        , @decimetros
        , @pies
        , @pies
        , getdate()
      )
    
  end
  
end
go
