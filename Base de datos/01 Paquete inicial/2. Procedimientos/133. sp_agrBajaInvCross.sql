use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrBajaInvCross')
begin 
  drop
    procedure sp_agrBajaInvCross
end
go

create procedure sp_agrBajaInvCross
(
	@noPiezas            int
	, @motivo            varchar (100)
	, @idInvPCross       int
)
as begin
	
	insert into
		tb_bajasInvCross
    
	values
		(
      @noPiezas
      , @motivo
      , getdate()
      , @idInvPCross
    )
end
go