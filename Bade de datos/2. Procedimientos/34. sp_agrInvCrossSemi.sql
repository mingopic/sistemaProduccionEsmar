use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrInvCrossSemi')
begin 
  drop
    procedure sp_agrInvCrossSemi
end
go

create procedure sp_agrInvCrossSemi
(
	@idInvPCross        int
	, @noPiezas         int
	, @noPiezasActuales int
)
as begin
	declare @fechaEntrada datetime
	
	set @fechaEntrada =
	(
		select
			getdate()
	)
	
	insert into
		tb_invCrossSemi
    
	values
		(
      @idInvPCross
      , @noPiezas
      , @noPiezasActuales
      , @fechaEntrada
    )
end
go