use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_ObtUltPartidaDet')
begin 
  drop
    procedure sp_ObtUltPartidaDet
end
go

create procedure sp_ObtUltPartidaDet
as begin
  
  select
    [idPartidaDet] = max(idPartidaDet)
    
  from
    tb_partidaDet
end
go