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
  , @accion int = 0
)
as begin
	
  if (@accion = 0)
  begin
  
		select  
			itc.noPartida
      , tr.descripcion as tipoRecorte
      , itc.noPiezasActuales
      , itc.kgTotalesActual
      , (itc.kgTotalesActual/itc.noPiezasActuales) as PesoPromXPza
      , itc.decimetrosActual
      , itc.piesActual
      , s.descripcion as seleccion
      , c.descripcion as calibre
      , itc.fechaEntrada
      , itc.idInvTerminado
      , itc.bandera
      
    from
	  tb_invTerminadoCompleto as itc
      
    inner join
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = itc.idTipoRecorte
      and tr.descripcion like @tipoRecorte
    
    inner join
      tb_calibre as c
    on
      c.idCalibre = itc.idCalibre
      and c.descripcion like @calibre
    
    inner join
      tb_seleccion as s
    on
      s.idSeleccion = itc.idSeleccion
      and s.descripcion like @seleccion
      
  where
    itc.fechaEntrada between @fecha and @fecha1
    and itc.noPiezasActuales > 0
    and 
    (
      (
        @noPartida = 0
        and itc.noPartida > 0
      )
      or
      (
        @noPartida > 0
        and itc.noPartida = @noPartida
      )
    )
	end
  
  else
	begin
  
		select  
			itc.noPartida
      , tr.descripcion as tipoRecorte
      , itc.noPiezasActuales
      , itc.kgTotalesActual
      , (itc.kgTotalesActual/itc.noPiezasActuales) as PesoPromXPza
      , itc.decimetrosActual
      , itc.piesActual
      , s.descripcion as seleccion
      , c.descripcion as calibre
      , itc.fechaEntrada
      , itc.idInvTerminado
      , itc.bandera
      
    from
	  tb_invTerminadoCompleto as itc
      
    inner join
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = itc.idTipoRecorte
      and tr.descripcion like @tipoRecorte
    
    inner join
      tb_calibre as c
    on
      c.idCalibre = itc.idCalibre
      and c.descripcion like @calibre
    
    inner join
      tb_seleccion as s
    on
      s.idSeleccion = itc.idSeleccion
      and s.descripcion like @seleccion
      
  where
    itc.fechaEntrada between @fecha and @fecha1
    and 
    (
      (
        @noPartida = 0
        and itc.noPartida > 0
      )
      or
      (
        @noPartida > 0
        and itc.noPartida = @noPartida
      )
    )
  end

end
go