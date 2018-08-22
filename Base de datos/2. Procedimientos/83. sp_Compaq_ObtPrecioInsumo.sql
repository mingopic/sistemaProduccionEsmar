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
  
  if @precio is null
  begin
    
    if @idProducto in (2138) /*AGUA*/
    begin
      set
        @precio = 0.4
    end
    /*
    else if @idProducto in (1480)
    begin
      set
        @precio = 49.92
    end
    
    else if @idProducto in (1477)
    begin
      set
        @precio = 37.44
    end
    
    else if @idProducto in (1478)
    begin
      set
        @precio = 37.82
    end
    
    else if @idProducto in (1481)
    begin
      set
        @precio = 36.48
    end
    
    else if @idProducto in (1473)
    begin
      set
        @precio = 24
    end
    
    else if @idProducto in (1472)
    begin
      set
        @precio = 33
    end
    */
    else begin
      set
        @precio = 0.0
    end
    
  end
  
  select
    [precio] = @precio
  
end
go