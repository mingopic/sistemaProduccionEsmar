use adESMAR2018
go

if exists (select name from sys.sysobjects WHERE name = 'sp_Compaq_ObtPrecioInsumo')
begin 
  drop
    procedure sp_Compaq_ObtPrecioInsumo
end
go

create procedure sp_Compaq_ObtPrecioInsumo
  (
    @idProducto int
  )
as begin
  
  declare
    @precio float
  
  select
    @precio = CPRECIOCAPTURADO
  
  from
    admMovimientos
    
  where
    CIDPRODUCTO = @idProducto
    and CIDMOVIMIENTO =
    (
      select
        max(CIDMOVIMIENTO)
      
      from 
        admMovimientos
      where
        CIDPRODUCTO = @idProducto
    )
  
  if @precio is null or @precio = 0
  begin
    
    if @idProducto =2138 /*AGUA*/
    begin
      set
        @precio = 0.04
    end
    
    else if @idProducto = 1480 /*SETA DE BLANK*/
    begin
      set
        @precio = 49.92
    end
    
    else if @idProducto = 1477 /*SETA SUN*/
    begin
      set
        @precio = 37.44
    end
    
    else if @idProducto = 1478 /*SETA SUPERSOL*/
    begin
      set
        @precio = 37.82
    end
    
    else if @idProducto = 1481 /*SETA RC*/
    begin
      set
        @precio = 36.48
    end
    
    else if @idProducto = 1473 /*RECURT FSA*/
    begin
      set
        @precio = 24
    end
    
    else if @idProducto = 1472 /*RECURT DIS 116*/
    begin
      set
        @precio = 33
    end
    
    else if @idProducto = 1476 /*SETA R25*/
    begin
      set
        @precio = 20.16
    end
    
    else begin
      set
        @precio = 0.0
    end
    
  end
  
  declare
    @idDocumento   int
    , @tipoMoneda  int
    , @importe     float
    
  set
    @idDocumento =
    (
      select
        CIDDOCUMENTO
      from
        admMovimientos
      where
        CIDPRODUCTO = @idProducto
        and CIDMOVIMIENTO =
        (
          select
            max(CIDMOVIMIENTO)
          
          from 
            admMovimientos
          where
            CIDPRODUCTO = @idProducto
        )
    )
  
  select
    @tipoMoneda = d.CIDMONEDA
    , @importe = tc.CIMPORTE
  from
    admTiposCambio as tc
  inner join
    admDocumentos as d
  on
    d.CIDMONEDA = tc.CIDMONEDA
    and tc.CFECHA =
    (
      select
        max(tc.CFECHA)
      from
        admTiposCambio as tc
    )
  where
    d.CIDDOCUMENTO = @idDocumento
  
  if (@importe = 1)
  begin
	select
		@importe = CIMPORTE
	from
		admTiposCambio
	where
	  CFECHA =
	  (
	    select
		  max(CFECHA)
	    from
		  admTiposCambio
		where
		  CIMPORTE > 1
	  )
  end
  
  if (@tipoMoneda = 2)
  begin
    set @precio = @precio * @importe
  end
  
  select
    [precio] = @precio
  
end
go