use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtEntInvSemTer')
begin 
  drop
    procedure sp_obtEntInvSemTer
end
go

create procedure sp_obtEntInvSemTer
(
	@tipoRecorte varchar(20)
)
as begin
	select
		tr.descripcion as tipoRecorte
		, sum(ist.noPiezasActuales) as noPiezas
    
	from
		tb_invSemTer as ist
    
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
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = pd.idTipoRecorte
      and tr.descripcion like @tipoRecorte
      
  where
    ist.noPiezasActuales > 0
    
	group by
		tr.descripcion
end