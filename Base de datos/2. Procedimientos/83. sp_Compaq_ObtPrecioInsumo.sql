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
    
    if @idProducto in (1,2)
    begin
      -- lo que tiene que hacer
    end
    
    else begin
      set
        @precio = 0.0
    end
    
  end
  
  select
    [precio] = @precio
  
end
go