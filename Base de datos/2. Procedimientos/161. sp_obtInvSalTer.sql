use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtInvSalTer')
begin 
  drop
    procedure sp_obtInvSalTer
end
go
	
create procedure sp_obtInvSalTer
as begin
		
    select	
      tr.descripcion as tipoRecorte
      , c.descripcion as calibre
      , s.descripcion as seleccion
      , isalt.noPiezas
      , isalt.kg
	  , isalt.decimetros
	  , isalt.pies
      , isalt.fechaEntrada
      , isalt.bandera
      , isalt.idInvSalTerminado
		
    from
	  tb_invSalTerminado as isalt
	inner join
	  tb_invTerminadoCompleto as itc
	on
	  itc.idInvTerminado = isalt.idInvTerminado
	  
    inner join
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = itc.idTipoRecorte
		
	  inner join
      tb_calibre as c
	  on
      c.idCalibre = itc.idCalibre

	  inner join
      tb_seleccion s
	  on
      s.idSeleccion = itc.idSeleccion
		
    where
      isalt.noPiezas > 0
end
go