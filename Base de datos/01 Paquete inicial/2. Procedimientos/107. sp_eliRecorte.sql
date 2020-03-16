use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_eliRecorte')
begin 
  drop
    procedure sp_eliRecorte
end
go

create procedure sp_eliRecorte
(
	@idPartidaDet          int
  , @NoPiezas            int
  , @idPartida           int
  , @idRecepcionCuero    int
  , @idTipoRecorte       int
)
as begin
  
  if (@idTipoRecorte in (2,3))
  begin
    
    delete from
      tb_partidaDet
    
    where
      idPartida = @idPartida
      and idRecepcionCuero = @idRecepcionCuero
      and idTipoRecorte in (2,3)
      and idProceso = 1
  end
  
  else begin
    
    delete from
      tb_partidaDet
    
    where
      idPartida = @idPartida
      and idRecepcionCuero = @idRecepcionCuero
      and idTipoRecorte = @idTipoRecorte
      and idProceso = 1
  end
  
  if (@idTipoRecorte in (2,3,4))
  begin
    
    if (@idTipoRecorte = 4)
    begin
      
      set
        @NoPiezas = @NoPiezas / 2
    end
    
    set
      @idTipoRecorte = 1 -- Entero
  end
  
  else if (@idTipoRecorte in (5,6))
  begin
    
    set
      @idTipoRecorte = 3 -- Crupon Sillero
    
    set
      @NoPiezas = @NoPiezas / 2
  end
  
  else if (@idTipoRecorte = 7)
  begin
    
    set
      @idTipoRecorte = 2 -- Delantero Sillero
    
    set
      @NoPiezas = @NoPiezas / 2
  end
  
  -- Regresar el número de piezas a su recorte anterior
  update
    tb_partidaDet
    
  set
    noPiezasAct = noPiezasAct + @NoPiezas
  
  where
    idPartida = @idPartida
    and idRecepcionCuero = @idRecepcionCuero
    and idTipoRecorte = @idTipoRecorte
    and idProceso = 1
end
go