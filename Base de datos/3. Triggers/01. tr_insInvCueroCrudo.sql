use esmarProd
go

if object_id('tr_insInvCueroCrudo') is not null
begin
	drop trigger tr_insInvCueroCrudo
end
go

create trigger tr_insInvCueroCrudo
  on tb_recepcionCuero
  after insert
as begin

  -- set nocount on impide que se generen mensajes de texto 
  set nocount on;

  insert into 
    tb_inventarioCrudo 
    (
      noPiezasActual
      , kgTotalActual
      , pesoXPieza
      , idRecepcionCuero
      , idTipoRecorte
    )
    
  select 
    noTotalPiezas
    , kgTotal
    , kgTotal / noTotalPiezas
    , idRecepcionCuero
    , 1
   
  from 
    tb_recepcionCuero
    
  where
    idRecepcionCuero = 
    (
      select 
        max(idRecepcionCuero) 
        
      from 
        tb_recepcionCuero
    )
end
go