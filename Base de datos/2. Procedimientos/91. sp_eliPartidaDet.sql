use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_eliPartidaDet')
begin 
  drop
    procedure sp_eliPartidaDet
end
go

create procedure sp_eliPartidaDet
(
	@idPartidaDet        int
  , @idInventarioCrudo int
)
as begin
  
  declare
    @noPiezas           int
    , @idPartida        int
    , @idRecepcionCuero int
  
  -- Obtener datos de la partidaDet 
  select
    @noPiezas = NoPiezas
    , @idPartida = idPartida
    , @idRecepcionCuero = idRecepcionCuero
    
  from
    tb_partidaDet
    
  where
    idPartidaDet = @idPartidaDet
  
  -- Eliminar partidaDet
  delete from
    tb_partidaDet
  
  where
    idPartidaDet = @idPartidaDet
  
  -- Si no existen más partidasDet se elimina la partida
  if not exists
  (
    select
      1
    from
      tb_partidaDet
    where
      idPartida = @idPartida
  )
  begin
    
    delete from
      tb_partida
      
    where
      idPartida = @idPartida
  end
  
  -- Actualizar noPiezas al inventario
  update
    tb_inventarioCrudo
  
  set
    noPiezasActual = noPiezasActual + @noPiezas
    , kgTotalActual = kgTotalActual + (pesoXPieza * @noPiezas)
  
  where
    idInventarioCrudo = @idInventarioCrudo
end
go