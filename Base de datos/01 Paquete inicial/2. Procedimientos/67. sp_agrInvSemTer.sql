use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrInvSemTer')
begin 
  drop
    procedure sp_agrInvSemTer
end
go

create procedure sp_agrInvSemTer
(
	@idInvSemiterminado int
	, @noPiezas         int
	, @noPiezasActuales int
	, @kgTotales 		float
)
as begin
	declare @fechaEntrada datetime
	
	set @fechaEntrada =
	(
		select
			getdate()
	)
	
	insert into
		tb_invSemTer
    
	values
	(
      @idInvSemiterminado
      , @noPiezas
      , @noPiezasActuales
	    , @kgTotales
      , @fechaEntrada
    )
end
go