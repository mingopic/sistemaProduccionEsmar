use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtAnioAct')
begin 
  drop
    procedure sp_obtAnioAct
end
go

create procedure sp_obtAnioAct
  as begin
  
    select 
      year(fecha) as anio
    from 
      tb_partida
    group by
      year(fecha)
  end
go