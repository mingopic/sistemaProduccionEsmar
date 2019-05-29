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
  
  update
    tb_partidaDet
  
  set
    noPiezasAct = noPiezasAct - @noPiezas
  
  where
    idPartidaDet = @idPartidaDet
end
go