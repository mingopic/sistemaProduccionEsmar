use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrDevolucion')
begin 
  drop
    procedure sp_agrDevolucion
end
go

create procedure sp_agrDevolucion
(
  @idInvSalTerminado  int
  , @noPiezas           int
  , @motivo             varchar(100)
	, @bandera						int
	, @kg								float
	, @decimetros				float
	, @pies							float
)
as begin

  declare @idInvTerminado int
	
	insert into
		tb_devolucion
		(
			idInvSalTerminado
			, noPiezas
			, motivo
			, fecha
			, kg
			, decimetros
			, pies
		)
		
	values
		(
			@idInvSalTerminado
			, @noPiezas
			, @motivo
			, getdate()
			, @kg
			, @decimetros
			, @pies
		)
		
	select
		@idInvTerminado = idInvTerminado
	from
		tb_invSalTerminado
	where
		idInvSalTerminado = @idInvSalTerminado
	
	if @bandera = 0
	begin
		update
			tb_invTerminado
		set
			noPiezasActuales = noPiezasActuales + @noPiezas
		where
			idInvTerminado = @idInvTerminado
	end
	
	else if @bandera = 1
	begin
		update
			tb_invTerminadoPesado
		set
			noPiezasActuales = noPiezasActuales + @noPiezas
		where
			idInvTerminadoPesado = @idInvTerminado
	end
	
	else if @bandera = 2
	begin
		update
			tb_invTerminadoManual
		set
			noPiezasActuales = noPiezasActuales + @noPiezas
		where
			idInvTerminadoManual = @idInvTerminado
	end
	
	update
		tb_invSalTerminado
	set
		noPiezasActuales = noPiezasActuales - @noPiezas
	where
		idInvSalTerminado = @idInvSalTerminado
end
go