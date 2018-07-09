use esmarProd
go

create procedure sp_obtNoCamion 
  (
    @idProveedor int
  )
  as begin
  
    declare @anioActual int
    declare @noCamion   int
    
    set @anioActual = 
    (
      Select
        year(getdate())
    )
      
    set @noCamion = 
    (
      select
        max(noCamion)
        
      from
        tb_recepcionCuero
        
      where 
        idProveedor = @idProveedor 
        and year(fechaEntrada) =  @anioActual
    )
    
    if(@noCamion is null) 
    begin
    
      set @noCamion = 1
    end
    
    else 
    begin
    
      set @noCamion = @noCamion+1
    end
    
    select @noCamion as noCamion
  end
go