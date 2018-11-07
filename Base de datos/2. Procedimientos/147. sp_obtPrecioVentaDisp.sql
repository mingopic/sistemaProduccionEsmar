use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtPrecioVentaDisp')
begin 
  drop
    procedure sp_obtPrecioVentaDisp
end
go

create procedure sp_obtPrecioVentaDisp
  (
    @idSeleccion     int
    , @idCalibre     int
    , @idTipoRecorte int
  )
  as begin
  
    select
      idPrecioVenta
    from
      tb_PrecioVenta
    where
      idSeleccion = @idSeleccion
    and
      idCalibre = @idCalibre
    and
      idTipoRecorte = @idTipoRecorte
  end
go