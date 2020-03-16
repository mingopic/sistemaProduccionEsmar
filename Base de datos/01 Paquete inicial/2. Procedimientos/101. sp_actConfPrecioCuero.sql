use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actConfPrecioCuero')
begin 
  drop
    procedure sp_actConfPrecioCuero
end
go

create procedure sp_actConfPrecioCuero
(
	@idConfPrecioCuero int
	, @porcentaje      float
)
as begin
  
  update
    tb_confPrecioCuero
  
  set
    porcentaje = @porcentaje
  
  where
    idConfPrecioCuero = @idConfPrecioCuero
end