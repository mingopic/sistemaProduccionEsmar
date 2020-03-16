use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtSalInvTermi')
begin 
  drop
    procedure sp_obtSalInvTermi
end
go

create procedure sp_obtSalInvTermi
(
	@tipoRecorte varchar(20)
	, @calibre varchar(20)
	, @seleccion varchar(20)
	, @noPartida int
	, @fecha varchar(10)
	, @fecha1 varchar(10)
)
as begin

	if(@noPartida = 0)
	begin
		select
			p.noPartida
			, tr.descripcion as tipoRecorte
			, isalt.noPiezas
      , isalt.kg
      , isalt.decimetros
      , isalt.pies
			, s.descripcion as seleccion
			, c.descripcion as calibre
			, isalt.fechaEntrada
      
		from
			tb_invSalTerminado as isalt
      
      inner join
        tb_invTerminado as it
      on
        it.idInvTerminado = isalt.idInvTerminado
      
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
			isalt.fechaEntrada between @fecha and @fecha1
      and isalt.bandera = 0
      
    --------------------------------------------------------------------------------------
    union all
    --------------------------------------------------------------------------------------
    
    select
			cp.noPartida
			, tr.descripcion as tipoRecorte
			, isalt.noPiezas
      , isalt.kg
      , isalt.decimetros
      , isalt.pies
			, s.descripcion as seleccion
			, c.descripcion as calibre
			, isalt.fechaEntrada
      
		from
			tb_invSalTerminado as isalt
      
      inner join
        tb_invTerminadoPesado as itp
      on
        itp.idInvTerminadoPesado = isalt.idInvTerminado
      
      inner join
        tb_invSemTerPesado as istp
      on
        istp.idInvSemTerPesado = itp.idInvSemTerPesado
        
      inner join
        tb_CueroPesado as cp
      on
        cp.idInventario = istp.idInventario
      
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = cp.idTipoRecorte
        and tr.descripcion like @tipoRecorte
      
      inner join
        tb_calibre as c
      on
        c.idCalibre = isalt.idCalibre
        and c.descripcion like @calibre
      
      inner join
        tb_seleccion as s
      on
        s.idSeleccion = isalt.idSeleccion
        and s.descripcion like @seleccion
		
		where
			isalt.fechaEntrada between @fecha and @fecha1
      and isalt.bandera = 1
      
    --------------------------------------------------------------------------------------
    union all
    --------------------------------------------------------------------------------------
    
    select
			0
			, tr.descripcion as tipoRecorte
			, isalt.noPiezas
      , isalt.kg
      , isalt.decimetros
      , isalt.pies
			, s.descripcion as seleccion
			, c.descripcion as calibre
			, isalt.fechaEntrada
      
		from
			tb_invSalTerminado as isalt
      
      inner join
        tb_invTerminadoManual as itm
      on
        itm.idInvTerminadoManual = isalt.idInvTerminado
      
      inner join
        tb_invSemTerManual as istm
      on
        istm.idInvSemTerManual = itm.idInvSemTerManual
      
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = istm.idTipoRecorte
        and tr.descripcion like @tipoRecorte
      
      inner join
        tb_calibre as c
      on
        c.idCalibre = isalt.idCalibre
        and c.descripcion like @calibre
      
      inner join
        tb_seleccion as s
      on
        s.idSeleccion = isalt.idSeleccion
        and s.descripcion like @seleccion
		
		where
			isalt.fechaEntrada between @fecha and @fecha1
      and isalt.bandera = 2
	end
	
	else ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	begin
		select
			p.noPartida
			, tr.descripcion as tipoRecorte
			, isalt.noPiezas
      , isalt.kg
      , isalt.decimetros
      , isalt.pies
			, s.descripcion as seleccion
			, c.descripcion as calibre
			, isalt.fechaEntrada
		from
			tb_invSalTerminado as isalt
		inner join
			tb_invTerminado as it
		on
			it.idInvTerminado = isalt.idInvTerminado
		
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
			isalt.fechaEntrada between @fecha and @fecha1
      and isalt.bandera = 0
    
    --------------------------------------------------------------------------------------
    union all
    --------------------------------------------------------------------------------------
    
    select
			cp.noPartida
			, tr.descripcion as tipoRecorte
			, isalt.noPiezas
      , isalt.kg
      , isalt.decimetros
      , isalt.pies
			, s.descripcion as seleccion
			, c.descripcion as calibre
			, isalt.fechaEntrada
      
		from
			tb_invSalTerminado as isalt
      
      inner join
        tb_invTerminadoPesado as itp
      on
        itp.idInvTerminadoPesado = isalt.idInvTerminado
      
      inner join
        tb_invSemTerPesado as istp
      on
        istp.idInvSemTerPesado = itp.idInvSemTerPesado
        
      inner join
        tb_CueroPesado as cp
      on
        cp.idInventario = istp.idInventario
        and cp.noPartida = @noPartida
      
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = cp.idTipoRecorte
        and tr.descripcion like @tipoRecorte
      
      inner join
        tb_calibre as c
      on
        c.idCalibre = isalt.idCalibre
        and c.descripcion like @calibre
      
      inner join
        tb_seleccion as s
      on
        s.idSeleccion = isalt.idSeleccion
        and s.descripcion like @seleccion
		
		where
			isalt.fechaEntrada between @fecha and @fecha1
      and isalt.bandera = 1
    
    --------------------------------------------------------------------------------------
    union all
    --------------------------------------------------------------------------------------
    
    select
		  0
			, tr.descripcion as tipoRecorte
			, isalt.noPiezas
      , isalt.kg
      , isalt.decimetros
      , isalt.pies
			, s.descripcion as seleccion
			, c.descripcion as calibre
			, isalt.fechaEntrada
      
		from
			tb_invSalTerminado as isalt
      
      inner join
        tb_invTerminadoManual as itm
      on
        itm.idInvTerminadoManual = isalt.idInvTerminado
      
      inner join
        tb_invSemTerManual as istm
      on
        istm.idInvSemTerManual = itm.idInvSemTerManual
      
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = istm.idTipoRecorte
        and tr.descripcion like @tipoRecorte
      
      inner join
        tb_calibre as c
      on
        c.idCalibre = isalt.idCalibre
        and c.descripcion like @calibre
      
      inner join
        tb_seleccion as s
      on
        s.idSeleccion = isalt.idSeleccion
        and s.descripcion like @seleccion
		
		where
			isalt.fechaEntrada between @fecha and @fecha1
      and isalt.bandera = 2
	end
end
go