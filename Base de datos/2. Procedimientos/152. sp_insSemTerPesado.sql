use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insSemTerPesado')
begin 
  drop
    procedure sp_insSemTerPesado
end
go

create procedure sp_insSemTerPesado
(
  @idInventario     int
  , @noPiezas         int
  , @noPiezasActuales int
  , @kgTotales        float
)
as begin
  
  insert into
    tb_invSemTerPesado
    
    values
    (
      @idInventario
      , @noPiezas
      , @noPiezasActuales
      , @kgTotales
      , getdate()
    )
end