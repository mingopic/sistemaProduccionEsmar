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
  , @proceso       varchar(20)
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