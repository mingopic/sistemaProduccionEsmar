use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_agrPartida')
begin 
  drop
    procedure sp_agrPartida
end
go
  
create procedure sp_agrPartida
(
  @noPartida       int
  , @noTotalPiezas int
  , @idProceso     int
)
as begin
  
  insert into
    tb_partida
    
  values
    (
      @noPartida
      , @noTotalPiezas
      , getdate()
      , @idProceso
    )
end
go