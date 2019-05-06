use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtInvSemTer')
begin 
  drop
    procedure sp_obtInvSemTer
end
go
	
create procedure sp_obtInvSemTer
as begin
		
    select
	  istc.noPartida
      , tr.descripcion as tipoRecorte
      , c.descripcion as calibre
      , s.descripcion as seleccion
      , istc.noPiezas
      , istc.kgTotales
      , istc.fechaEntrada
      , istc.bandera
      , istc.idInvSemTer
		
    from
      tb_invSemTerCompleto as istc  
    inner join
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = istc.idTipoRecorte
		
	  inner join
      tb_calibre as c
	  on
      c.idCalibre = istc.idCalibre

	  inner join
      tb_seleccion s
	  on
      s.idSeleccion = istc.idSeleccion
		
    where
      istc.noPiezas > 0
end
go