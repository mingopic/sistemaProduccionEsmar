use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_valBorrarRecorte')
begin 
  drop
    procedure sp_valBorrarRecorte
end
go

create procedure sp_valBorrarRecorte
(
	@idPartidaDet       int
  , @NoPiezas         int
  , @idPartida        int
  , @idRecepcionCuero int
  , @idTipoRecorte    int
)
as begin
  
  declare
    @borrar int
  
  /*
   2 - Delantero Sillero
   3 - Crupon Sillero
  */
  if (@idTipoRecorte in (2,3))
  begin 
    
    declare
      @NoPiezasDelantero      int
      , @NoPiezasActDelantero int
      , @NoPiezasCrupon       int
      , @NoPiezasActCrupon    int
      
    select
      @NoPiezasDelantero = noPiezas
      , @NoPiezasActDelantero  = noPiezasAct
    
    from
      tb_partidaDet
    
    where
      idPartida = @idPartida
      and idRecepcionCuero = @idRecepcionCuero
      and idTipoRecorte = 2
      and idProceso = 1

    -- Si ya se utlizaron piezas de ese recorte regresa la bandera "borrar" con 0 para indicar que no se puede eliminar
    if (@NoPiezasDelantero not in (@NoPiezasActDelantero))
    begin
      
      set
      @borrar = 0
      
      select 
        [borrar] = @borrar
    end
    
    else begin
    
      select
        @NoPiezasCrupon = noPiezas
        , @NoPiezasActCrupon  = noPiezasAct
      
      from
        tb_partidaDet
      
      where
        idPartida = @idPartida
        and idRecepcionCuero = @idRecepcionCuero
        and idTipoRecorte = 3
        and idProceso = 1
      
      -- Si ya se utlizaron piezas de ese recorte regresa la bandera "borrar" con 0 para indicar que no se puede eliminar
      if (@NoPiezasCrupon not in (@NoPiezasActCrupon))
      begin
        
        set
          @borrar = 0
          
          select 
            [borrar] = @borrar
      end
        
      else begin
      
        set
          @borrar = 1
          
          select 
            [borrar] = @borrar
      end
    end
      
  end
  
  else begin
    -- Validar que no se haya usado ese recorte
    if 
    (
      @NoPiezas = 
        (
          select 
            noPiezas
          from
            tb_partidaDet
          where
            idPartidaDet = @idPartidaDet
        )
    )
    begin
    
      set
        @borrar = 1
        
      select 
        [borrar] = @borrar
    end
    
    else begin
    
      set
        @borrar = 0
        
      select 
        [borrar] = @borrar
    end
    
  end
  
end
go