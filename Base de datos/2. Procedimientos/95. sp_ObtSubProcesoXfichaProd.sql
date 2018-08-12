use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_ObtSubProcesoXfichaProd')
begin 
  drop
    procedure sp_ObtSubProcesoXfichaProd
end
go

create procedure sp_ObtSubProcesoXfichaProd
(
	@idFichaProd int
)
as begin

  select
    sp.descripcion

  from
    tb_InsumosFichaProd as ifp

  inner join
    tb_subProceso as sp
  on
    sp.idSubproceso = ifp.idSubproceso
    
  where
    ifp.idFichaProd = @idFichaProd
end
go