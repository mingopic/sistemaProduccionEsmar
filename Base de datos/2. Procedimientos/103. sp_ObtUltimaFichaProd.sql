use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_ObtUltimaFichaProd')
begin 
  drop
    procedure sp_ObtUltimaFichaProd
end
go

create procedure sp_ObtUltimaFichaProd
as begin
  
  select
    [idFichaProd] = max(idFichaProd)
  
  from
    tb_fichaProd
end
go