use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtEntInvCrossSemi')
begin 
  drop
    procedure sp_obtEntInvCrossSemi
end
go

create procedure sp_obtEntInvCrossSemi
(
	@tipoRecorte varchar(20)
)
as begin
	select
		tr.descripcion as tipoRecorte
		, sum(ics.noPiezasActuales) as noPiezas
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
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = pd.idTipoRecorte
      and tr.descripcion like @tipoRecorte
      
  where
    ics.noPiezasActuales > 0
    
	group by
		tr.descripcion
end