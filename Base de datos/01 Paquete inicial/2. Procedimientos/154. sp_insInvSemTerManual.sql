use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insInvSemTerManual')
begin 
  drop
    procedure sp_insInvSemTerManual
end
go

create procedure sp_insInvSemTerManual
as begin

	delete from
    tb_invSemTerManual
  
  insert into 
    tb_invSemTerManual
    (
      idTipoRecorte
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
    TIPORECORTE
    , CALIBRE
    , SELECCION
    , NOPIEZAS
    , NOPIEZAS
    , KGTOTAL
    , KGTOTAL
    , DMTOTAL
    , DMTOTAL
    , PIESTOTAL
    , PIESTOTAL
    , getdate()
       
  from 
    INVENTARIOPRODUCTOTERMINADO$
  
  where
    NOPIEZAS > 0
  
end
go