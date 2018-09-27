use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtPartidaXproceso')
begin 
  drop
    procedure sp_obtPartidaXproceso
end
go

create procedure sp_obtPartidaXproceso
(
	@idProceso int
)
as begin

	if @idProceso in (2,3,4)
  begin
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
      and pd.idProceso = @idProceso - 1
  end
  
  else
  begin
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
      and pd.idProceso = @idProceso - 1
  end
end