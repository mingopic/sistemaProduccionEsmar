use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtFechaServer')
begin 
  drop
    procedure sp_obtFechaServer
end
go
	
create procedure sp_obtFechaServer
as begin
  
  declare 
    @fecha date
    
	select
    @fecha = getdate()
    
  select
    [fecha] = @fecha
end
go