use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrBajaInvSemiterminado')
begin 
  drop
    procedure sp_agrBajaInvSemiterminado
end
go

create procedure sp_agrBajaInvSemiterminado
(
	@noPiezas             int
	, @motivo             varchar (100)
	, @idInvSemiterminado int
)
as begin
	
	insert into
		tb_bajasInvSemiterminado
    
	values
		(
      @noPiezas
      , @motivo
      , getdate()
      , @idInvSemiterminado
    )
end
go