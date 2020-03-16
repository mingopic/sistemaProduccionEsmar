use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insInvTerminadoManual')
begin 
  drop
    procedure sp_insInvTerminadoManual
end
go

create procedure sp_insInvTerminadoManual
as begin
  
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
  
  select 
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
    , getdate()
       
  from 
    tb_invSemTerManual
  
  where
    noPiezasActuales > 0 and (idCalibre <> 20 or idSeleccion <> 21)
  
end
go