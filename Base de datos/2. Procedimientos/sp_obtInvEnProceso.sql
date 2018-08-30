use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtInvEnProceso')
begin 
  drop
    procedure sp_obtInvEnProceso
end
go

create procedure sp_obtInvEnProceso
(
	@idProceso  int
)
as begin

  select
    [Piezas] = sum(pd.noPiezasAct)
    , tr.descripcion as 'TipoRecorte'
    , pr.descripcion as 'Proceso'
    , pr.idProceso

  from
    tb_partidaDet pd

    inner join
      tb_tipoRecorte as tr
    on
      tr.idTipoRecorte = pd.idTipoRecorte
      
    inner join
      tb_proceso pr
    on
      pr.idProceso = pd.idProceso

  where
    pd.noPiezasAct > 0
    and 
    (
      (
        @idProceso = 0
        and pd.idProceso not in (1,7)
      )
      or
      (
        @idProceso <> 0
        and pd.idProceso = @idProceso
      )
    )

  group by
    tr.descripcion
    , pr.descripcion
    , pr.idProceso

  order by
    pr.idProceso asc

end