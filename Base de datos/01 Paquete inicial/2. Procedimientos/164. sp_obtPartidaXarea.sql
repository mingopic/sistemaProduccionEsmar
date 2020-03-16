use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtPartidaXarea')
begin 
  drop
    procedure sp_obtPartidaXarea
end
go

create procedure sp_obtPartidaXarea
(
	@area varchar(30)
)
as begin

	declare @idProceso int

	if @area in ('REMOJO','PELAMBRE','DESENCALADO')
  begin
		select @idProceso = 
			idProceso
		from
			tb_proceso
		where
			descripcion = @area
		
    select
      pa.NoPartida
      , case
          when tr.descripcion = 'Entero' then 'Entero'
          when tr.descripcion = 'Delantero Sillero' then 'Delantero'
          when tr.descripcion = 'Crupon Sillero' then 'Crupon'
          when tr.descripcion = 'Lados' then 'Lados'
          when tr.descripcion = 'Centro Castaño' then 'Centro Castaño'
          when tr.descripcion = 'Centro Quebracho' then 'Centro Quebracho'
          when tr.descripcion = 'Centro' then 'Centro'
          when tr.descripcion = 'Delantero Suela' then 'Delantero Suela'
        end as descripcion
      , pd.noPiezasAct
      , pd.idPartidaDet
      , pa.idPartida
      , tr.idTipoRecorte
      , [Proveedor] = pr.nombreProveedor + ' - ' + cast(rc.noCamion as varchar)
      , rc.idRecepcionCuero
      , pd.idRecortePartidaDet
      , pd.idInventarioCrudo
			, pd.idPartidaDet as idDescontar
      
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
      and pd.idProceso = @idProceso
  end
  
  else if @area in ('CURTIDO','ENGRASE')
  begin
		select @idProceso = 
			idProceso
		from
			tb_proceso
		where
			descripcion = @area
		
    select
      pa.NoPartida
      , tr.descripcion
      , pd.noPiezasAct
      , pd.idPartidaDet
      , pa.idPartida
      , tr.idTipoRecorte
      , [Proveedor] = pr.nombreProveedor + ' - ' + cast(rc.noCamion as varchar)
      , rc.idRecepcionCuero
      , pd.idRecortePartidaDet
      , pd.idInventarioCrudo
			, pd.idPartidaDet as idDescontar
      
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
      and pd.idProceso = @idProceso
  end
	
	else if @area = 'DESVENADO'
	begin
		select 
      p.noPartida
			, [Proveedor] = pr.nombreProveedor + ' - ' + cast(rc.noCamion as varchar)
      , tr.descripcion
      , ic.noPiezasActuales as noPiezasAct
			, pd.idPartidaDet
			, p.idPartida
			, tr.idTipoRecorte
			, ic.idInvPCross as idDescontar
      
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
			
			inner join
				tb_recepcionCuero as rc
			on
				rc.idRecepcionCuero = pd.idRecepcionCuero
			
			inner join
				tb_proveedor as pr
			on
				pr.idProveedor = rc.idProveedor
        
    where
      ic.noPiezasActuales > 0
	end
	
	else if @area = 'SEMITERMINADO'
	begin
		select  
			p.noPartida
			, [Proveedor] = pr.nombreProveedor + ' - ' + cast(rc.noCamion as varchar)
      , tr.descripcion as descripcion
      , ins.noPiezasActuales as noPiezasAct
			, pd.idPartidaDet
			, p.idPartida
			, tr.idTipoRecorte
			, ins.idInvSemiterminado as idDescontar
      
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
      
      inner join
        tb_calibre as c
      on
        c.idCalibre = ins.idCalibre
      
      inner join
        tb_seleccion as s
      on
        s.idSeleccion = ins.idSeleccion
			
			inner join
				tb_recepcionCuero as rc
			on
				rc.idRecepcionCuero = pd.idRecepcionCuero
			
			inner join
				tb_proveedor as pr
			on
				pr.idProveedor = rc.idProveedor
        
    where
      ins.noPiezasActuales > 0
	end
	
	else if @area = 'TERMINADO'
	begin
		select  
			itc.noPartida
			, [Proveedor] = pr.nombreProveedor + ' - ' + cast(rc.noCamion as varchar)
      , tr.descripcion as descripcion
      , itc.noPiezasActuales as noPiezasAct
			, pd.idPartidaDet
			, p.idPartida
			, tr.idTipoRecorte
			, itc.idInvTerminado as idDescontar
      
    from
	  tb_invTerminadoCompleto as itc
      
    inner join
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = itc.idTipoRecorte
    
    inner join
      tb_calibre as c
    on
      c.idCalibre = itc.idCalibre
    
    inner join
      tb_seleccion as s
    on
      s.idSeleccion = itc.idSeleccion
		
		inner join
			tb_invTerminado as it
		on
			it.idInvTerminado = itc.idInvTerminado
			
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
			tb_recepcionCuero as rc
		on
			rc.idRecepcionCuero = pd.idRecepcionCuero
		
		inner join
			tb_proveedor as pr
		on
			pr.idProveedor = rc.idProveedor
      
  where
    itc.noPiezasActuales > 0
	end
end