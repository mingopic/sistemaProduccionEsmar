use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_ObtUltIdInsumoFichaProd')
begin 
  drop
    procedure sp_ObtUltIdInsumoFichaProd
end
go

create procedure sp_ObtUltIdInsumoFichaProd
as begin

  select
    [idInsumoFichaProd] = max(idInsumoFichaProd)
  
  from
    tb_InsumosFichaProd
end
go