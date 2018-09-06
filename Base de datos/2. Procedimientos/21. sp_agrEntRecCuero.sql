use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrEntRecCuero')
begin 
  drop
    procedure sp_agrEntRecCuero
end
go

create procedure sp_agrEntRecCuero
(
  @idProveedor       int
  ,@noCamion         int
  ,@idTipoCuero      int
  ,@idRangoPesoCuero int
  ,@noPiezasLigero   int
  ,@noPiezasPesado   int
  ,@noTotalPiezas    int
  ,@kgTotal          float
  ,@precioXKilo      float
  ,@mermaSal         float
  ,@mermaHumedad     float
  ,@mermaCachete     float
  ,@mermaTarimas     float
  ,@refParamerma     int
  ,@idMerSal         int
  ,@idMerHum         int
  ,@idMerCac         int
  ,@idMerTar         int
  ,@tipoCamion       varchar(50)
)
as begin
  
  declare @fechaEntrada datetime
  declare @costoCamion float
  declare @precioGarra float
  
  set @fechaEntrada =
    (
      select
        getdate()
    )
  
  set @costoCamion = @kgTotal * @precioXKilo
  
  select
    @precioGarra = costo
  
  from
    tb_costoGarra
    
  where
    fecha =
      (
        select 
          max(fecha)
        from
          tb_costoGarra
      )
  
  insert into
    tb_recepcionCuero
    
  values
    (
      @idProveedor
      , @noCamion
      , @idTipoCuero
      , @idRangoPesoCuero
      , @noPiezasLigero
      , @noPiezasPesado
      , @noTotalPiezas
      , @kgTotal
      , @precioXKilo
      , @costoCamion
      , @precioGarra
      , @mermaSal
      , @mermaHumedad
      , @mermaCachete
      , @mermaTarimas
      , @refParamerma
      , @idMerSal
      , @idMerHum
      , @idMerCac
      , @idMerTar
      , @tipoCamion
      , @fechaEntrada
    )
end
go