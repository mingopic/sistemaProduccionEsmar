use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrInvSemi')
begin 
  drop
    procedure sp_agrInvSemi
end
go

create procedure sp_agrInvSemi
(
	@idInvCrossSemi int
	, @idCalibre    int
	, @idSeleccion  int
  , @noPiezas     int
	, @kgTotales    float
)
as begin

	insert into
		tb_invSemiterminado
    
	values
		(
      @idInvCrossSemi
      , @idCalibre
      , @idSeleccion
      , @noPiezas
      , @noPiezas
      , @kgTotales
      , @kgTotales
      , getdate()
    )
end
go