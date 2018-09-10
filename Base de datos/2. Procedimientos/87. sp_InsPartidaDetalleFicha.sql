use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_InsPartidaDetalleFicha')
begin 
  drop
    procedure sp_InsPartidaDetalleFicha
end
go

create procedure sp_InsPartidaDetalleFicha
(
  @noPiezas        int
  , @idPartida     int
  , @idPartidaDet  int
  , @idTipoRecorte int
)
as begin

  declare 
    @idRecepcionCuero    int
    , @idProceso         int
    , @idInventarioCrudo int 
  
  select
    @idRecepcionCuero = idRecepcionCuero
    , @idProceso = idProceso
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
      , @idProceso + 1
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