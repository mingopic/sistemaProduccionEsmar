use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrInvTer')
begin 
  drop
    procedure sp_agrInvTer
end
go

create procedure sp_agrInvTer
(
	@idInvSemTer int
	, @idCalibre    int
	, @idSeleccion  int
	, @noPiezas     int
	, @kgTotales    float
)
as begin

	insert into
		tb_invTerminado
    
	values
		(
      @idInvSemTer
      , @idCalibre
      , @idSeleccion
      , @noPiezas
      , @noPiezas
      , @kgTotales
      , getdate()
    )
end
go