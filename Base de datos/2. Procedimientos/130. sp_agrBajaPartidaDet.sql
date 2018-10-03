use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrBajaPartidaDet')
begin 
  drop
    procedure sp_agrBajaPartidaDet
end
go

create procedure sp_agrBajaPartidaDet
(
	@noPiezas            int
	, @motivo            varchar (100)
	, @idPartidaDet      int
)
as begin
	
	insert into
		tb_bajasPartidaDet
    
	values
		(
      @noPiezas
      , @motivo
      , getdate()
      , @idPartidaDet
    )
end
go