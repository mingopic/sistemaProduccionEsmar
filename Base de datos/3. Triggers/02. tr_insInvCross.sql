use esmarProd
go

if object_id('tr_insInvCross') is not null
begin
	drop trigger tr_insInvCross
end
go

create trigger tr_insInvCross
  on tb_fichaProdDet
  after insert
as begin
  
  declare
    @idPartidaDet int
    , @idPartida  int
    , @noPiezas   int
    , @idProceso  int
    , @kg         float
  
  select
    @idPartidaDet = idPartidaDet
    , @kg = kgTotal
  
  from
    inserted
    
  select
    @idPartida = idPartida
    , @noPiezas = noPiezas
    , @idProceso = idProceso
  
  from
    tb_partidaDet
  
  where
    idPartidaDet = @idPartidaDet
    
  if @idProceso = 6
  begin
  
    insert into
      tb_invCross
      (
        idPartidaDet     
        , idPartida        
        , noPiezas         
        , noPiezasActuales
        , kgTotal
        , kgActual
        , fechaEntrada   
      )
    values
      (
        @idPartidaDet
        , @idPartida
        , @noPiezas
        , @noPiezas
        , @kg
        , @kg
        , getdate()
      )
  end
  
end
go