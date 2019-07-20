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
    , @idTipoMoneda	 int
    , @identificaRecorte varchar(30)
  )
  as begin
      
    if (@idTipoRecorte = 7)
    begin
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
      and
        idTipoMoneda = @idTipoMoneda
      and 
        identificaRecorte = @identificaRecorte
    end
    
    else 
    begin
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
      and
        idTipoMoneda = @idTipoMoneda
    end
    
  end
go