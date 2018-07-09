use esmarProd
go

create procedure sp_obtSalidaCross
as begin

	select
		ic.idPartida
    , tr.descripcion
    , ics.noPiezas
    , ics.fechaEntrada
    
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
      
    inner join
      tb_invCrossSemi as ics
    on
      ic.idInvPCross = ics.idInvPCross
end
go