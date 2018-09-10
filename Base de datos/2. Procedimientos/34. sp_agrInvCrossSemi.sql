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
  , @kg               float
)
as begin
	
	insert into
		tb_invCrossSemi
    
	values
		(
      @idInvPCross
      , @noPiezas
      , @noPiezasActuales
      , @kg
      , @kg
      , getdate()
    )
end
go