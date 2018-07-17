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
    , pd.noPiezasAct * (rec.kgTotal/rec.noTotalPiezas) as 'KgTotal'
    , (rec.kgTotal/rec.noTotalPiezas) as 'PesoProm'
    , pd.idPartidaDet
    , pa.idPartida
    
	from
		tb_partidaDet as pd
    
    inner join
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = pd.idTipoRecorte
      
    inner join
      tb_partida as pa
    on
      pa.idPartida = pd.idPartidaDet
      and pa.idProceso = @idProceso -1
      
    inner join
      tb_recepcionCuero as rec
    on
      rec.idRecepcionCuero = pd.idRecepcionCuero
  
	where
		pd.noPiezasAct > 0
end