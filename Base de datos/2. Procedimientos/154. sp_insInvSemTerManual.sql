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
      , fechaEntrada
    )
  
  select 
    RECORTE
    , CALIBRE
    , SELECCION
    , TOTAL119
    , TOTAL211
    , getdate()
       
  from 
    INVENTARIOPRODUCTOTERMINADO$
  
  where
    TOTAL211 > 0
  
end
go