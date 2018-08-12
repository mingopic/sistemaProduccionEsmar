use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_ObtInsumosFichaProd')
begin 
  drop
    procedure sp_ObtInsumosFichaProd
end
go

create procedure sp_ObtInsumosFichaProd
(
  @idInsumoFichaProd int
)
as begin

  select
    clave
    , porcentaje
    , Material
    , Temperatura
    , Rodar
    , cantidad
    , Observaciones
    , PrecioUnitario
    , total
  
  from
    tb_InsumosFichaProdDet
  
  where
    idInsumoFichaProd = @idInsumoFichaProd
end
go