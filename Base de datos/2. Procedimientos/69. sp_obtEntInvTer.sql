use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtEntInvTer')
begin 
  drop
    procedure sp_obtEntInvTer
end
go

create procedure sp_obtEntInvTer
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
      , it.noPiezas
      , it.noPiezasActuales
      , it.kgTotales
      , it.kgTotalesActual
      , (it.kgTotales/it.noPiezas) as PesoPromXPza
      , it.decimetros
      , it.decimetrosActual
      , it.pies
      , it.piesActual
      , s.descripcion as seleccion
      , c.descripcion as calibre
      , it.fechaEntrada
      , it.idInvTerminado
      
    from
	  tb_invTerminado as it
	inner join
	  tb_invSemTer as ist
	on
	  ist.idInvSemTer = it.idInvSemTer
	  
	inner join
      tb_invSemiterminado as ins
	on
	  ins.idInvSemiterminado = ist.idInvSemiterminado
      
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
        c.idCalibre = it.idCalibre
        and c.descripcion like @calibre
      
      inner join
        tb_seleccion as s
      on
        s.idSeleccion = it.idSeleccion
        and s.descripcion like @seleccion
        
    where
      it.fechaEntrada between @fecha and @fecha1
      
	end
  
  else
	begin
  
		select  
			p.noPartida
      , tr.descripcion as tipoRecorte
      , it.noPiezas
      , it.noPiezasActuales
      , it.kgTotales
      , (it.kgTotales/it.noPiezas) as PesoPromXPza
      , it.decimetros
      , it.pies
      , s.descripcion as seleccion
      , c.descripcion as calibre
      , it.fechaEntrada
      , it.idInvTerminado
      
    from
	  tb_invTerminado as it
	inner join
	  tb_invSemTer as ist
	on
	  ist.idInvSemTer = it.idInvSemTer
	  
	inner join
      tb_invSemiterminado as ins
	on
	  ins.idInvSemiterminado = ist.idInvSemiterminado
      
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
        c.idCalibre = it.idCalibre
        and c.descripcion like @calibre
      
      inner join
        tb_seleccion as s
      on
        s.idSeleccion = it.idSeleccion
        and s.descripcion like @seleccion
        
    where
      it.fechaEntrada between @fecha and @fecha1
  end

end
go