use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actCuePesado')
begin 
  drop
    procedure sp_actCuePesado
end
go

create procedure sp_actCuePesado
(
	@idInventario       int
  , @peso             float
	, @noPiezasActuales float
)
as begin
  
  update
    tb_CueroPesado
  
  set
    noPiezasActuales = noPiezasActuales-@noPiezasActuales
    , kg = kg-@peso
  
  where
    idInventario = @idInventario
  
  
  insert into
    tb_invSemTerPesado
    (
      idInventario
      , noPiezas
      , noPiezasActuales
      , kgTotales
      , fechaEntrada
    )
    
    values
    (
      @idInventario
      , @noPiezasActuales
      , @noPiezasActuales
      , @peso
      , getdate()
    )
end