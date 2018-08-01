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
	, @noPiezas         int
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
    
	values
	(
      @idInvTerminado
      , @noPiezas
      , @fechaEntrada
    )
end
go