use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtPartidaXproceso')
begin 
  drop
    procedure sp_obtPartidaXproceso
end
go

create procedure sp_obtPartidaXproceso
(
	@idProceso int
)
as begin

	select
		pa.NoPartida
		, tr.descripcion
		, pd.noPiezasAct
    , pd.idPartidaDet
    , pa.idPartida
    , tr.idTipoRecorte
    
	from
		tb_partidaDet as pd
    
    inner join
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = pd.idTipoRecorte
      
    inner join
      tb_partida as pa
    on
      pa.idPartida = pd.idPartida
  
	where
		pd.noPiezasAct > 0
    and pd.idProceso = @idProceso - 1
end