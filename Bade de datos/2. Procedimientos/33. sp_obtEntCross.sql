use esmarProd
go

create procedure sp_obtEntCross
(
	@tipoRecorte varchar(20)
	, @noPartida int
	, @fecha varchar(10)
	, @fecha1 varchar(10)
)
as begin

	if (@noPartida = 0)
	begin
  
		select 
			p.noPartida
      , tr.descripcion
      , ic.noPiezas
      , ic.noPiezasActuales
      , ic.fechaentrada
      , ic.idInvPCross
      
		from 
			tb_invCross as ic
      
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
		  ic.fechaentrada between @fecha and @fecha1
	end
	
	else
	begin
		
    select 
			p.noPartida
      , tr.descripcion
      , ic.noPiezas
      , ic.noPiezasActuales
      , ic.fechaentrada
      , ic.idInvPCross
      
		from 
			tb_invCross as ic
      
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
		  ic.fechaentrada between @fecha and @fecha1
  end
  
end
go