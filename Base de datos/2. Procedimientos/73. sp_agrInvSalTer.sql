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
      , noPiezas
      , kg
      , decimetros
      , pies
      , fechaEntrada
    )
    
	values
	(
    @idInvTerminado
    , @noPiezas
    , @kg
    , @decimetros
    , @pies
    , @fechaEntrada
  )
end
go