use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_InsPartidaDetalleFichaReproceso')
begin 
  drop
    procedure sp_InsPartidaDetalleFichaReproceso
end
go

create procedure sp_InsPartidaDetalleFichaReproceso
(
  @noPiezas        int
  , @idPartida     int
  , @idPartidaDet  int
  , @idTipoRecorte int
	, @idProceso		 int
	, @area					 varchar(30)
	, @idDescontar	 int
)
as begin

  declare 
    @idRecepcionCuero    int
    , @idInventarioCrudo int 
  
  select
    @idRecepcionCuero = idRecepcionCuero
    , @idInventarioCrudo = idInventarioCrudo
    
  from
    tb_partidaDet
    
  where
    idPartidaDet = @idPartidaDet
  
  insert into
    tb_partidaDet
    (
      noPiezas
      , noPiezasAct
      , idPartida
      , idRecepcionCuero
      , idTipoRecorte
      , idProceso
      , idInventarioCrudo
      , procedenciaCrudo
      , idRecortePartidaDet
      
    )
    
  values
    (
      @noPiezas
      , @noPiezas
      , @idPartida
      , @idRecepcionCuero
      , @idTipoRecorte
      , @idProceso
      , @idInventarioCrudo
      , 0
      , @idPartidaDet
    )
  
	if @area in ('REMOJO','PELAMBRE','DESENCALADO', 'CURTIDO', 'ENGRASE')
	begin
		update
			tb_partidaDet
		
		set
			noPiezasAct = noPiezasAct - @noPiezas
		
		where
			idPartidaDet = @idPartidaDet
	end
	
	else if @area = 'DESVENADO'
	begin
		update
			tb_invCross
		
		set
			noPiezasActuales = noPiezasActuales - @noPiezas
		
		where
			idInvPCross = @idDescontar
	end
	
	else if @area = 'SEMITERMINADO'
	begin
		update
			tb_invSemiterminado
		
		set
			noPiezasActuales = noPiezasActuales - @noPiezas
		
		where
			idInvSemiterminado = @idDescontar
	end
	
	else if @area = 'TERMINADO'
	begin
		declare @bandera int
		
		select
			@bandera = bandera
		from
			tb_invTerminadoCompleto
		where
			idInvTerminado = @idDescontar
		
		if @bandera = 0
		begin
			update
			tb_invTerminado
		
			set
				noPiezasActuales = noPiezasActuales - @noPiezas
			
			where
				idInvTerminado = @idDescontar
		end
		
		else if @bandera = 1
		begin
			update
			tb_invTerminadoPesado
		
			set
				noPiezasActuales = noPiezasActuales - @noPiezas
			
			where
				idInvTerminadoPesado = @idDescontar
		end
		
		else if @bandera = 2
		begin
			update
			tb_invTerminadoManual
		
			set
				noPiezasActuales = noPiezasActuales - @noPiezas
			
			where
				idInvTerminadoManual = @idDescontar
		end
	end
end
go