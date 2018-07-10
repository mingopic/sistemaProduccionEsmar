use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtEntInvSem')
begin 
  drop
    procedure sp_obtEntInvSem
end
go

create procedure sp_obtEntInvSem
(
	@tipoRecorte varchar(20)
	, @calibre varchar(15)
	, @seleccion varchar(15)
	, @noPartida int
	, @fecha varchar(10)
	, @fecha1 varchar(10)
)
as begin
	
  if (@noPartida = 0)
  begin
  
		select  
			p.noPartida
      , tr.descripcion as tipoRecorte
      , ins.noPiezas
      , ins.noPiezasActuales
      , ins.kgTotales
      , (ins.kgTotales/ins.noPiezas) as PesoPromXPza
      , s.descripcion as seleccion
      , c.descripcion as calibre
      , ins.fechaEntrada
      , ins.idInvSemiterminado
      
    from
      tb_invSemiterminado as ins
      
      inner join
        tb_invCrossSemi as ics
      on
        ics.idInvCrossSemi = ins.idInvCrossSemi
      
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
      
      inner join
        tb_calibre as c
      on
        c.idCalibre = ins.idCalibre
        and c.descripcion like @calibre
      
      inner join
        tb_seleccion as s
      on
        s.idSeleccion = ins.idSeleccion
        and s.descripcion like @seleccion
        
    where
      ins.fechaEntrada between @fecha and @fecha1
      
	end
  
  else
	begin
  
		select  
			p.noPartida
      , tr.descripcion as tipoRecorte
      , ins.noPiezas
      , ins.noPiezasActuales
      , ins.kgTotales
      , (ins.kgTotales/ins.noPiezas) as PesoPromXPza
      , s.descripcion as seleccion
      , c.descripcion as calibre
      , ins.fechaEntrada
      , ins.idInvSemiterminado
      
    from
      tb_invSemiterminado as ins
      
      inner join
        tb_invCrossSemi as ics
      on
        ics.idInvCrossSemi = ins.idInvCrossSemi
      
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
      
      inner join
        tb_calibre as c
      on
        c.idCalibre = ins.idCalibre
        and c.descripcion like @calibre
      
      inner join
        tb_seleccion as s
      on
        s.idSeleccion = ins.idSeleccion
        and s.descripcion like @seleccion
        
    where
      ins.fechaEntrada between @fecha and @fecha1
  end

end
go