use esmarProd
go
	
create procedure sp_obtInvCrossSemi
(
	@tipoRecorte varchar(20)
	, @noPartida int
	, @fecha     varchar(10)
	, @fecha1    varchar(10)
)
as begin

	if (@noPartida = 0)
	begin
		
    select
			p.noPartida
      , tr.descripcion
      , ics.noPiezas
      , ics.noPiezasActuales
      , ics.fechaEntrada
      , ics.idInvCrossSemi
		
    from
			tb_invCrossSemi as ics
		
      inner join
        tb_invCross as ic
      on
        ic.idInvPCross = ics.idInvPCross
      
      inner join
        tb_partidaDet as pd
      on
        pd.idPartidaDet = ic.idPartidaDet
      
      inner join
        tb_partida as p
      on
        p.idPartida = pd.idPartida
        
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = pd.idTipoRecorte
        and tr.descripcion like @tipoRecorte
		
    where
      ics.fechaentrada between @fecha and @fecha1
      and ics.noPiezasActuales > 0
	end
	
	else
	begin
		select
			p.noPartida
      , tr.descripcion
      , ics.noPiezas
      , ics.noPiezasActuales
      , ics.fechaEntrada
      , ics.idInvCrossSemi
		
    from
			tb_invCrossSemi as ics
		
      inner join
        tb_invCross as ic
      on
        ic.idInvPCross = ics.idInvPCross
      
      inner join
        tb_partidaDet as pd
      on
        pd.idPartidaDet = ic.idPartidaDet
      
      inner join
        tb_partida as p
      on
        p.idPartida = pd.idPartida
        and p.noPartida = @noPartida
        
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = pd.idTipoRecorte
        and tr.descripcion like @tipoRecorte
		
    where
      ics.fechaentrada between @fecha and @fecha1
      and ics.noPiezasActuales > 0
	end
  
end
go