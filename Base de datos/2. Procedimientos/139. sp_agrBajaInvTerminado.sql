use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrBajaInvTerminado')
begin 
  drop
    procedure sp_agrBajaInvTerminado
end
go

create procedure sp_agrBajaInvTerminado
(
	@noPiezas         int
	, @motivo         varchar (100)
	, @idInvTerminado int
  , @bandera        int
)
as begin
	
	insert into
		tb_bajasInvTerminado
    
	values
		(
      @noPiezas
      , @motivo
      , getdate()
      , @idInvTerminado
      , @bandera
    )
end
go