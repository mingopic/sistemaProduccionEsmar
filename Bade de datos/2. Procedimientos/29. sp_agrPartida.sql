use esmarProd
go
  
create procedure sp_agrPartida
(
  @noPartida       int
  , @noTotalPiezas int
  , @idProceso     int
)
as begin

  declare @fecha datetime
  
  set @fecha = 
  (
    select getdate()
  )
  
  insert into
    tb_partida
    
  values
    (
      @noPartida
      , @noTotalPiezas
      , @fecha
      , @idProceso
    )
end
go