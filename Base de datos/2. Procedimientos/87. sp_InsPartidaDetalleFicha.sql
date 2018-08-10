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
    @idRecepcionCuero int
    , @idProceso      int
  
  select
    @idRecepcionCuero = idRecepcionCuero
    , @idProceso = idProceso
    
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
    )
    
  values
    (
      @noPiezas
      , @noPiezas
      , @idPartida
      , @idRecepcionCuero
      , @idTipoRecorte
      , @idProceso + 1
    )
  
  update
    tb_partidaDet
  
  set
    noPiezasAct = noPiezasAct - @noPiezas
  
  where
    idPartidaDet = @idPartidaDet
end
go