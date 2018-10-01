use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrBajaInvCrudo')
begin 
  drop
    procedure sp_agrBajaInvCrudo
end
go

create procedure sp_agrBajaInvCrudo
(
	@noPiezas            int
	, @motivo            varchar (100)
	, @idInventarioCrudo int
)
as begin
	
	insert into
		tb_bajasInventarioCrudo
    
	values
		(
      @noPiezas
      , @motivo
      , getdate()
      , @idInventarioCrudo
    )
end
go