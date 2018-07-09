use esmarProd
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
      , getdate()
    )
end
go