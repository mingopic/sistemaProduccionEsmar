use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_InsInsumosFichaProdDet')
begin 
  drop
    procedure sp_InsInsumosFichaProdDet
end
go

create procedure sp_InsInsumosFichaProdDet
(
	@idInsumoFichaProd int
  , @clave           varchar(50)
  , @porcentaje      float
  , @material        varchar(60)
  , @temperatura     varchar(50)
  , @rodar           varchar(50)
  , @cantidad        float
  , @observaciones   varchar(100)
  , @precioUnitario  float
  , @total           float
)
as begin

  insert into
    tb_InsumosFichaProdDet
    (
      idInsumoFichaProd
      , clave
      , porcentaje
      , material
      , temperatura
      , rodar
      , cantidad
      , observaciones
      , precioUnitario
      , total
    )
    
  values
    (
      @idInsumoFichaProd
      , @clave
      , @porcentaje
      , @material
      , @temperatura
      , @rodar
      , @cantidad
      , @observaciones
      , @precioUnitario
      , @total
    )
end
go