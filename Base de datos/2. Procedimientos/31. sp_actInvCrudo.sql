use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actInvCrudo')
begin 
  drop
    procedure sp_actInvCrudo
end
go

create procedure sp_actInvCrudo
(
  @proveedor        varchar(20)
  , @noCamion       int
  , @fecha          date
  , @piezasUtilizar int
  , @kgDescontar	float
)
as begin

  declare @idRecepcionCuero int
  
  set @idRecepcionCuero =
    (
      select
        idRecepcionCuero
        
      from
        tb_recepcionCuero
        
      where
        noCamion = @noCamion
        and fechaEntrada = @fecha
        and idProveedor =
          (
            select
              idProveedor
              
            from
              tb_proveedor
              
            where
              nombreProveedor = @proveedor
          )
    )
    
  update
    tb_inventarioCrudo
    
  set
    noPiezasActual = noPiezasActual-@piezasUtilizar
	, kgTotalActual = kgTotalActual-@kgDescontar
    
  where
    idRecepcionCuero = @idRecepcionCuero
end
go