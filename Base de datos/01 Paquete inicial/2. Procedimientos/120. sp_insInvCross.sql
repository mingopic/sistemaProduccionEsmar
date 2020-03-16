use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insInvCross')
begin 
  drop
    procedure sp_insInvCross
end
go

create procedure sp_insInvCross
(
  @idPartidaDet int
  , @idPartida  int 
  , @noPiezas   int
)
as begin
  
  declare
    @kg float
    
  select
    @kg = @noPiezas * (kgTotal/noPiezasTotal)
    
  from
    tb_fichaProdDet
    
  where
    idPartidaDet = @idPartidaDet
    
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
  
  update
    tb_partidaDet
  
  set
    noPiezasAct = noPiezasAct - @noPiezas
  
  where
    idPartidaDet = @idPartidaDet
    
end
go