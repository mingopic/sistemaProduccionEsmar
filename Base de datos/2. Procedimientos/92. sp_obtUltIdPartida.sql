use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtUltIdPartida')
begin 
  drop
    procedure sp_obtUltIdPartida
end
go

create procedure sp_obtUltIdPartida
as begin

  select
    max(idPartida) as idPartida
  
  from
    tb_partida
end
go