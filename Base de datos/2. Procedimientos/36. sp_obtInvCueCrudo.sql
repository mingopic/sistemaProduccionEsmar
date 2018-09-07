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
  declare @porcentajeCenCastano   float
  declare @porcentajeCenQuebracho float
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
    
  -- Centro Castano
  select
    @porcentajeCenCastano = porcentaje
  from
    tb_confPrecioCuero
  where
    idTipoRecorte = 5
    
  -- Centro Quebracho
  select
    @porcentajeCenQuebracho = porcentaje
  from
    tb_confPrecioCuero
  where
    idTipoRecorte = 6
  
  -- Centro Quebracho
  select
    @porcentajeDelSuela = porcentaje
  from
    tb_confPrecioCuero
  where
    idTipoRecorte = 7
  
	select
		tr.descripcion + ' ' + tc.descripcion as descripcion
    , p.nombreProveedor
    , ic.noPiezasActual
    , ic.kgTotalActual
    , (ic.kgTotalActual/ic.noPiezasActual) as pesoProm
    , case
        when ic.idTipoRecorte = 1 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeEntero
        when ic.idTipoRecorte = 2 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeDelSillero
        when ic.idTipoRecorte = 3 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeCrupSillero
        when ic.idTipoRecorte = 4 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeLados
        when ic.idTipoRecorte = 5 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeCenCastano
        when ic.idTipoRecorte = 6 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeCenQuebracho
        when ic.idTipoRecorte = 7 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeDelSuela
      end as precioXPza
    , (
        ic.noPiezasActual * 
         (
          case
            when ic.idTipoRecorte = 1 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeEntero
            when ic.idTipoRecorte = 2 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeDelSillero
            when ic.idTipoRecorte = 3 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeCrupSillero
            when ic.idTipoRecorte = 4 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeLados
            when ic.idTipoRecorte = 5 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeCenCastano
            when ic.idTipoRecorte = 6 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeCenQuebracho
            when ic.idTipoRecorte = 7 then ((rc.costoCamion - ((rc.noTotalPiezas * 2) * rc.precioGarra)) / rc.noTotalPiezas) * @porcentajeDelSuela
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