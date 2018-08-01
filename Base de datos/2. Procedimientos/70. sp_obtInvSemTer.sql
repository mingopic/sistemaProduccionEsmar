use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtInvSemTer')
begin 
  drop
    procedure sp_obtInvSemTer
end
go
	
create procedure sp_obtInvSemTer
(
	@tipoRecorte varchar(20)
	, @calibre	 varchar(20)
	, @seleccion varchar(20)
	, @noPartida int
	, @fecha     varchar(10)
	, @fecha1    varchar(10)
)
as begin

	if (@noPartida = 0)
	begin
		
    select
			p.noPartida
      , tr.descripcion as tipoRecorte
	  , c.descripcion as calibre
	  , s.descripcion as seleccion
      , ist.noPiezas
      , ist.noPiezasActuales
	  , ist.kgTotales
      , ist.fechaEntrada
      , ist.idInvSemTer
		
    from
		tb_invSemTer as ist
	  inner join
		tb_invSemiterminado as ins
	  on
		ins.idInvSemiterminado = ist.idInvSemiterminado
		
	  inner join
		tb_invCrossSemi as ics
	  on ics.idInvCrossSemi = ins.idInvCrossSemi
		
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
		tb_calibre c
	  on
		c.idCalibre = ins.idCalibre
		and c.descripcion like @calibre

	  inner join
		tb_seleccion s
	  on
		s.idSeleccion = ins.idSeleccion
		and s.descripcion like @seleccion
		
    where
      ist.fechaEntrada between @fecha and @fecha1
      and ist.noPiezasActuales > 0
	end
	
	else
	begin
		select
			p.noPartida
      , tr.descripcion as tipoRecorte
	  , c.descripcion as calibre
	  , s.descripcion as seleccion
      , ist.noPiezas
      , ist.noPiezasActuales
      , ist.fechaEntrada
      , ist.idInvSemTer
		
    from
		tb_invSemTer as ist
	  inner join
		tb_invSemiterminado as ins
	  on
		ins.idInvSemiterminado = ist.idInvSemiterminado
		
	  inner join
		tb_invCrossSemi as ics
	  on ics.idInvCrossSemi = ins.idInvCrossSemi
		
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
		tb_calibre c
	  on
		c.idCalibre = ins.idCalibre
		and c.descripcion like @calibre

	  inner join
		tb_seleccion s
	  on
		s.idSeleccion = ins.idSeleccion
		and s.descripcion like @seleccion
		
    where
      ist.fechaEntrada between @fecha and @fecha1
      and ist.noPiezasActuales > 0
	end
  
end
go