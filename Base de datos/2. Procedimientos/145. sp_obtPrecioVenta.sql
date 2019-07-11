use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtPrecioVenta')
begin 
  drop
    procedure sp_obtPrecioVenta
end
go

create procedure sp_obtPrecioVenta
(
  @idTipoRecorte    int
  , @idCalibre     int
  , @idSeleccion int
)
as begin
  
  select
    case
	  when pv.idTipoMoneda = 2 then tri.descripcion
	  when tr.descripcion = 'Delantero Sillero' then 'Del. Sillero Novillo/Del. Sillero Toro'
	  when tr.descripcion = 'Crupon Sillero' then 'Crupon Sillero'
	  when tr.descripcion = 'Centro Castaño' then 'Centro Castaño'
	  when tr.descripcion = 'Centro Quebracho' then 'Centro Quebracho'
	  when tr.descripcion = 'Delantero Suela' then 'Delantero'
	  when tr.descripcion = 'Sottopiede' then 'Sottopiede'
	  else tr.descripcion 
	end as tipoRecorte
    , c.descripcion as calibre
    , s.descripcion as seleccion
    , pv.precio_original
	, pv.precio_credito
	, pv.precio_buffed
    , tm.descripcion as moneda
    , um.descripcion
    , pv.fecha
    , pv.idPrecioVenta
    
  from
    tb_PrecioVenta as pv
    
  inner join
    tb_unidadMedida as um
  on
    um.idUnidadMedida = pv.idUnidadMedida
    
  inner join
    tb_tipoRecorte as tr
  on
    pv.idTipoRecorte = tr.idTipoRecorte
  
  inner join
    tb_calibre as c
  on
    pv.idCalibre = c.idCalibre
    
  inner join
    tb_seleccion as s
  on
    pv.idSeleccion = s.idSeleccion
    
  inner join
    tb_tipoMoneda as tm
  on
    pv.idTipoMoneda = tm.idTipoMoneda
  
  inner join
    tb_tipoRecorte_ingles tri
  on
    tr.idTipoRecorte = tri.idTipoRecorte
	
  where
  (
    (
      @idSeleccion = 0
      and pv.idSeleccion > 0
    )
    or
    (
      @idSeleccion > 0
      and pv.idSeleccion = @idSeleccion
    )
  )
  and
  (
    (
      @idCalibre = 0
      and pv.idCalibre > 0
    )
    or
    (
      @idCalibre > 0
      and pv.idCalibre = @idCalibre
    )
  )
  and
  (
    (
      @idTipoRecorte = 0
      and pv.idTipoRecorte > 0
    )
    or
    (
      @idTipoRecorte > 0
      and pv.idTipoRecorte = @idTipoRecorte
    )
  )
end
go