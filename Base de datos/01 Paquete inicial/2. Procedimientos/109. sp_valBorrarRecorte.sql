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
	@idPartidaDet          int
  , @NoPiezas            int
  , @idTipoRecorte       int
  , @idRecortePartidaDet int
)
as begin
  
  declare
    @borrar int
  
  -- validar si el origen del recorte viene desde el inventario crudo
  if (@idRecortePartidaDet <> 0)
  begin
  
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
        idRecortePartidaDet = @idRecortePartidaDet
        and idProceso = 1
        and idTipoRecorte = 2

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
          idRecortePartidaDet = @idRecortePartidaDet
          and idProceso = 1
          and idTipoRecorte = 3
        
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
    
    -- 4 - Lados
    else if (@idTipoRecorte = 4)
    begin 
      
      declare
        @NoPiezasCuero           int
        , @NoPiezasActualesCuero int
      
      if exists
       (
         select
           1
         from
           tb_partidaDet
         where
           idTipoRecorte = 5
       )
       begin
         
        select
          @NoPiezasCuero = noPiezas
          , @NoPiezasActualesCuero  = noPiezasAct
        
        from
          tb_partidaDet
        
        where
          idRecortePartidaDet = @idRecortePartidaDet
          and idProceso = 1
          and idTipoRecorte = 5
       end
       
       else begin
       
        select
          @NoPiezasCuero = noPiezas
          , @NoPiezasActualesCuero  = noPiezasAct
        
        from
          tb_partidaDet
        
        where
          idRecortePartidaDet = @idRecortePartidaDet
          and idProceso = 1
          and idTipoRecorte = 6
       end

      -- Si ya se utlizaron piezas de ese recorte regresa la bandera "borrar" con 0 para indicar que no se puede eliminar
      if (@NoPiezas not in (@NoPiezasActualesCuero))
      begin
        
        set
        @borrar = 0
        
        select 
          [borrar] = @borrar
      end
      
      else begin
      
        select
          @NoPiezasCuero = noPiezas
          , @NoPiezasActualesCuero  = noPiezasAct
        
        from
          tb_partidaDet
        
        where
          idRecortePartidaDet = @idRecortePartidaDet
          and idProceso = 1
          and idTipoRecorte = 7
        
        -- Si ya se utlizaron piezas de ese recorte regresa la bandera "borrar" con 0 para indicar que no se puede eliminar
        if (@NoPiezasCuero not in (@NoPiezasActualesCuero))
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
  
  else begin
    
    -- Regresa 2 para indicar que no se puede eliminar recorte que ya ha sido recortado desde inventario crudo
    set
      @borrar = 2
      
    select 
      [borrar] = @borrar
  end
  
end
go