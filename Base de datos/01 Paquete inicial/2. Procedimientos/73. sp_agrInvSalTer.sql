use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrInvSalTer')
begin 
  drop
    procedure sp_agrInvSalTer
end
go

create procedure sp_agrInvSalTer
(
	@idInvTerminado 	int
	, @noPiezas       int
  , @kg             float
  , @decimetros     float
  , @pies           float
  , @idCalibre      int
  , @idSeleccion    int
  , @bandera        int
)
as begin
	declare @fechaEntrada datetime
	
	set @fechaEntrada =
	(
		select
			getdate()
	)
	
	insert into
		tb_invSalTerminado
    (
      idInvTerminado
      , bandera
      , idCalibre
      , idSeleccion
      , noPiezas
			, noPiezasActuales
      , kg
      , decimetros
      , pies
      , fechaEntrada
    )
    
	values
	(
    @idInvTerminado
    , @bandera
    , @idCalibre
    , @idSeleccion
    , @noPiezas
		, @noPiezas
    , @kg
    , @decimetros
    , @pies
    , @fechaEntrada
  )
end
go