use esmarProd
go

if object_id('tr_insInvCross') is not null
begin
	drop trigger tr_insInvCross
end
go

create trigger tr_insInvCross
  on tb_partidaDet
  after insert
as begin
  
  declare
    @idPartidaDet int
    , @idPartida  int
    , @noPiezas   int
    , @idProceso  int
    
    
  select
    @idPartidaDet = idPartidaDet
    , @idPartida = idPartida
    , @noPiezas = noPiezas
    , @idProceso = idProceso
  
  from
    inserted
    
  if @idProceso = 6
  begin
  
    insert into
      tb_invCross
      (
        idPartidaDet     
        , idPartida        
        , noPiezas         
        , noPiezasActuales 
        , fechaEntrada     
      )
    values
      (
        @idPartidaDet
        , @idPartida
        , @noPiezas
        , @noPiezas
        , getdate()
      )
  end
  
end
go