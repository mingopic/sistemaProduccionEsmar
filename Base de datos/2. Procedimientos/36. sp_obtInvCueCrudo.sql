use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtInvCueCrudo')
begin 
  drop
    procedure sp_obtInvCueCrudo
end
go

create procedure sp_obtInvCueCrudo
as begin
  
  declare @porcentajeEntero       float
  declare @porcentajeDelSillero   float
  declare @porcentajeCrupSillero  float
  declare @porcentajeLados        float
  declare @porcentajeCentro       float
  declare @porcentajeDelSuela     float
  
  -- Entero
  select
    @porcentajeEntero = porcentaje
  from
    tb_confPrecioCuero
  where
    idTipoRecorte = 1
  
  -- Delantero Sillero
  select
    @porcentajeDelSillero = porcentaje
  from
    tb_confPrecioCuero
  where
    idTipoRecorte = 2
  
  -- Crupon Sillero
  select
    @porcentajeCrupSillero = porcentaje
  from
    tb_confPrecioCuero
  where
    idTipoRecorte = 3
    
  -- Lados
  select
    @porcentajeLados = porcentaje
  from
    tb_confPrecioCuero
  where
    idTipoRecorte = 4
    
  -- Centro
  select
    @porcentajeCentro = porcentaje
  from
    tb_confPrecioCuero
  where
    idTipoRecorte = 8
  
  -- Delantero Suela
  select
    @porcentajeDelSuela = porcentaje
  from
    tb_confPrecioCuero
  where
    idTipoRecorte = 7
  
	select
    case
      when tr.descripcion = 'Entero' then 'Entero' + ' ' + tc.descripcion
      when tr.descripcion = 'Delantero Sillero' then 'Delantero' + ' ' + tc.descripcion
      when tr.descripcion = 'Crupon Sillero' then 'Crupon' + ' ' + tc.descripcion
      when tr.descripcion = 'Lados' then 'Lados' + ' ' + tc.descripcion
      when tr.descripcion = 'Centro' then 'Centro' + ' ' + tc.descripcion
      when tr.descripcion = 'Delantero Suela' then 'Delantero Suela' + ' ' + tc.descripcion
    end as descripcion
    , p.nombreProveedor
    , ic.noPiezasActual
    , ic.kgTotalActual
    , (ic.kgTotalActual/ic.noPiezasActual) as pesoProm
    , case
        when ic.idTipoRecorte = 1 then ((rc.costoCamion / rc.noTotalPiezas) * @porcentajeEntero)
        when ic.idTipoRecorte = 2 then ((rc.costoCamion / rc.noTotalPiezas) - (rc.precioGarra * 2)) * @porcentajeDelSillero
        when ic.idTipoRecorte = 3 then ((rc.costoCamion / rc.noTotalPiezas) - (rc.precioGarra * 2)) * @porcentajeCrupSillero
        when ic.idTipoRecorte = 4 then ((rc.costoCamion / rc.noTotalPiezas) * @porcentajeLados)
        when ic.idTipoRecorte = 8 then ((rc.costoCamion / rc.noTotalPiezas) - (rc.precioGarra * 2)) * @porcentajeCentro
        when ic.idTipoRecorte = 7 then ((rc.costoCamion / rc.noTotalPiezas) - (rc.precioGarra * 2)) * @porcentajeDelSuela
      end as precioXPza
    , (
        ic.noPiezasActual * 
         (
          case
            when ic.idTipoRecorte = 1 then ((rc.costoCamion / rc.noTotalPiezas) * @porcentajeEntero)
            when ic.idTipoRecorte = 2 then ((rc.costoCamion / rc.noTotalPiezas) - (rc.precioGarra * 2)) * @porcentajeDelSillero
            when ic.idTipoRecorte = 3 then ((rc.costoCamion / rc.noTotalPiezas) - (rc.precioGarra * 2)) * @porcentajeCrupSillero
            when ic.idTipoRecorte = 4 then ((rc.costoCamion / rc.noTotalPiezas) * @porcentajeLados)
            when ic.idTipoRecorte = 8 then ((rc.costoCamion / rc.noTotalPiezas) - (rc.precioGarra * 2)) * @porcentajeCentro
            when ic.idTipoRecorte = 7 then ((rc.costoCamion / rc.noTotalPiezas) - (rc.precioGarra * 2)) * @porcentajeDelSuela
          end
         )
      ) as costoCamion
    
	from
		tb_inventarioCrudo as ic
    
    inner join
      tb_recepcionCuero as rc
    on
      rc.idRecepcionCuero = ic.idRecepcionCuero
      
    inner join
      tb_tipoRecorte tr
    on
      tr.idTipoRecorte = ic.idTipoRecorte

    
    inner join
      tb_tipoCuero as tc
    on
      tc.idTipoCuero = rc.idTipoCuero
      
    inner join
      tb_proveedor as p
    on
      p.idProveedor = rc.idProveedor
      
  where
    ic.noPiezasActual > 0
end
go