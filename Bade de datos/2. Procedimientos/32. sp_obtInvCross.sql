use esmarProd
go

create procedure sp_obtInvCross
as begin

	select
		tr.descripcion
    , ic.noPiezasActuales
    
	from
		tb_tipoRecorte as tr
    
    inner join
      tb_partidaDet as pd
    on
      tr.idTipoRecorte = pd.idTipoRecorte
      
    inner join
      tb_invCross as ic
    on
      pd.idPartidaDet = ic.idPartidaDet
      
	where
		ic.noPiezasActuales > 0
end
go