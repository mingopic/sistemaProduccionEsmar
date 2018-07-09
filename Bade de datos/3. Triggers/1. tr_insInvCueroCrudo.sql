use esmarProd
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
      , idRecepcionCuero
    )
    
  select 
    noTotalPiezas
    , idRecepcionCuero
    
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