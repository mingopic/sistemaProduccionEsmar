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
  @idFichaProd int
)
as begin
  
  declare
    @idInsumoFichaProd int
  
  select
    @idInsumoFichaProd = idInsumoFichaProd
  
  from
    tb_InsumosFichaProd
  
  where
    idFichaProd = @idFichaProd
   
   
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