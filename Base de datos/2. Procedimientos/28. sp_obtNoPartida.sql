use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtNoPartida')
begin 
  drop
    procedure sp_obtNoPartida
end
go

create procedure sp_obtNoPartida
as begin

  declare @anioActual int
  declare @noPartida int
  
  set @anioActual = 
  (
    Select
      year(getdate())
  )
    
  set @noPartida = 
  (
    select
      max(noPartida)
      
    from
      tb_partida
      
    where year(fecha) =  @anioActual
  )
  
  if(@noPartida is null) 
  begin
  
    set @noPartida = 1
  end
  
  else 
  begin
  
    set @noPartida = @noPartida+1
  end
  
  select @noPartida as noPartida
end
go