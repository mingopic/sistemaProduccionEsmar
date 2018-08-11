use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtPartDetEli')
begin 
  drop
    procedure sp_obtPartDetEli
end
go

create procedure sp_obtPartDetEli
(
	@idPartidaDet int
  , @idPartida int
)
as begin

  if not exists
  (
    select
      1
    from
      tb_partidaDet
    where
      idPartida = @idPartida
      and idProceso > 1
  )
  begin
    select
      1 as eliminar
  end
  
  else
  begin
    select 0 as eliminar
  end
end
go