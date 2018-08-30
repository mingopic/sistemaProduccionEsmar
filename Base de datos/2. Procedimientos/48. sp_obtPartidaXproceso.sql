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
    , [Proveedor] = pr.nombreProveedor + ' - ' + cast(rc.noCamion as varchar)
    , rc.idRecepcionCuero
    
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
    
    inner join
      tb_recepcionCuero rc
    on
      rc.idRecepcionCuero = pd.idRecepcionCuero
    
    inner join
      tb_proveedor pr
    on
      pr.idProveedor = rc.idProveedor
    
	where
		pd.noPiezasAct > 0
    and pd.idProceso = @idProceso - 1
end