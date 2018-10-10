use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insRecorte')
begin 
  drop
    procedure sp_insRecorte
end
go

create procedure sp_insRecorte
  (
    @idPartidaDet int
    , @idTipoRecorte int
    , @noPiezasAct int
    , @noPiezas int
    , @idPartida int
    , @idProceso int
  )
  as begin
    
    declare 
      @idRecepcionCuero  int
      , @idInventarioCrudo int
      , @garra            float
      , @garraDesc        float
      
    update
      tb_partidaDet
    
    set
      noPiezasAct = noPiezasAct - @noPiezasAct
    
    where
      idPartidaDet = @idPartidaDet
    
    --
    select
      @idRecepcionCuero = idRecepcionCuero
      , @idInventarioCrudo = idInventarioCrudo
      
    from
      tb_partidaDet
      
    where
      idPartidaDet = @idPartidaDet
        
      
    if @idTipoRecorte = 0
    begin
    
      insert into
        tb_partidaDet
        (
          noPiezas
          , noPiezasAct
          , idPartida
          , idRecepcionCuero
          , idTipoRecorte
          , idProceso
          , idInventarioCrudo
          , procedenciaCrudo
          , idRecortePartidaDet
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , 2
          , @idProceso
          , @idInventarioCrudo
          , 0
          , @idPartidaDet
        )
        
      insert into
        tb_partidaDet
        (
          noPiezas
          , noPiezasAct
          , idPartida
          , idRecepcionCuero
          , idTipoRecorte
          , idProceso
          , idInventarioCrudo
          , procedenciaCrudo
          , idRecortePartidaDet
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , 3
          , @idProceso
          , @idInventarioCrudo
          , 0
          , @idPartidaDet
        )
      
      set
        @garra =
        (
          select
            costo
            
          from
            tb_costoGarra
            
          where
            idCostoGarra =
            (
              select
                max(idCostoGarra)
                
              from
                tb_costoGarra
            )
        )
      
      set
        @garraDesc = (@garra*2) * @noPiezasAct
      
      update
        tb_fichaProdDet
      set
        costoTotalCuero = costoTotalCuero - @garraDesc
      where
        idPartidaDet = @idPartidaDet
    end
    
    else if @idTipoRecorte = 1
    begin
    
      insert into
        tb_partidaDet
        (
          noPiezas
          , noPiezasAct
          , idPartida
          , idRecepcionCuero
          , idTipoRecorte
          , idProceso
          , idInventarioCrudo
          , procedenciaCrudo
          , idRecortePartidaDet
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , 5
          , @idProceso
          , @idInventarioCrudo
          , 0
          , @idPartidaDet
        )
        
        insert into
          tb_partidaDet
          (
            noPiezas
            , noPiezasAct
            , idPartida
            , idRecepcionCuero
            , idTipoRecorte
            , idProceso
            , idInventarioCrudo
            , procedenciaCrudo
            , idRecortePartidaDet
          )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , 7
          , @idProceso
          , @idInventarioCrudo
          , 0
          , @idPartidaDet
        )
        
      set
        @garra =
        (
          select
            costo
            
          from
            tb_costoGarra
            
          where
            idCostoGarra =
            (
              select
                max(idCostoGarra)
                
              from
                tb_costoGarra
            )
        )
      
      set
        @garraDesc = @garra * @noPiezasAct
      
      update
        tb_fichaProdDet
      set
        costoTotalCuero = costoTotalCuero - @garraDesc
      where
        idPartidaDet = @idPartidaDet
    end
    
    else if @idTipoRecorte = 2
    begin
    
      insert into
        tb_partidaDet
        (
          noPiezas
          , noPiezasAct
          , idPartida
          , idRecepcionCuero
          , idTipoRecorte
          , idProceso
          , idInventarioCrudo
          , procedenciaCrudo
          , idRecortePartidaDet
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , 6
          , @idProceso
          , @idInventarioCrudo
          , 0
          , @idPartidaDet
        )
        
      insert into
        tb_partidaDet
        (
          noPiezas
          , noPiezasAct
          , idPartida
          , idRecepcionCuero
          , idTipoRecorte
          , idProceso
          , idInventarioCrudo
          , procedenciaCrudo
          , idRecortePartidaDet
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , 7
          , @idProceso
          , @idInventarioCrudo
          , 0
          , @idPartidaDet
        )
        
        set
        @garra =
        (
          select
            costo
            
          from
            tb_costoGarra
            
          where
            idCostoGarra =
            (
              select
                max(idCostoGarra)
                
              from
                tb_costoGarra
            )
        )
      
      set
        @garraDesc = @garra * @noPiezasAct
      
      update
        tb_fichaProdDet
      set
        costoTotalCuero = costoTotalCuero - @garraDesc
      where
        idPartidaDet = @idPartidaDet
    end
    
    else begin
      
      insert into
        tb_partidaDet
        (
          noPiezas
          , noPiezasAct
          , idPartida
          , idRecepcionCuero
          , idTipoRecorte
          , idProceso
          , idInventarioCrudo
          , procedenciaCrudo
          , idRecortePartidaDet
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , @idTipoRecorte
          , @idProceso 
          , @idInventarioCrudo
          , 0
          , @idPartidaDet
        )
    end
    
  end
go


