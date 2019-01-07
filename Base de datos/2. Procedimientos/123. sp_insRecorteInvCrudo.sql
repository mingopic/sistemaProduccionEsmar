use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insRecorteInvCrudo')
begin 
  drop
    procedure sp_insRecorteInvCrudo
end
go

create procedure sp_insRecorteInvCrudo
  (
    @idInventarioCrudo  int
    , @noPiezasOriginal int
    , @noPiezas1        int
    , @noPiezas2        int
    , @kg1              float
    , @kg2              float
    , @idTipoRecorte    int
    , @idRecepcionCuero int
  )
  as begin
    
    declare
      @promkgXPiezaEntera float
    
    set
      @promkgXPiezaEntera =
      (
        select
          pesoXPieza
        from
          tb_inventarioCrudo
        where
          idInventarioCrudo = @idInventarioCrudo
      )
      
    if (@idTipoRecorte = 0 or @idTipoRecorte = 4)
    begin
      update
        tb_inventarioCrudo
      
      set
        noPiezasActual = noPiezasActual - @noPiezasOriginal
        , KgTotalActual = (noPiezasActual - @noPiezasOriginal) * @promkgXPiezaEntera
      
      where
        idInventarioCrudo = @idInventarioCrudo
    end
    
    else
    begin
      update
        tb_inventarioCrudo
      
      set
        noPiezasActual = noPiezasActual - @noPiezasOriginal
        , KgTotalActual = KgTotalActual - @kg1 - @kg2
      
      where
        idInventarioCrudo = @idInventarioCrudo
    end
      
    if @idTipoRecorte = 0
    begin
    
      insert into
        tb_inventarioCrudo
        (
          noPiezasActual
          , KgTotalActual
          , pesoXPieza
          , idRecepcionCuero
          , idTipoRecorte
        )
        
      values
        (
          @noPiezas1
          , @kg1
          , @kg1 / @noPiezas1
          , @idRecepcionCuero
          , 2
        )
        
      insert into
        tb_inventarioCrudo
        (
          noPiezasActual
          , KgTotalActual
          , pesoXPieza
          , idRecepcionCuero
          , idTipoRecorte
        )
      
      values
        (
          @noPiezas2
          , @kg2
          , @kg2 / @noPiezas2
          , @idRecepcionCuero
          , 3
        )
    end
    
    else if @idTipoRecorte = 1
    begin
    
      insert into
        tb_inventarioCrudo
        (
          noPiezasActual
          , KgTotalActual
          , pesoXPieza
          , idRecepcionCuero
          , idTipoRecorte
        )
        
      values
        (
          @noPiezas1
          , @kg1
          , @kg1 / @noPiezas1
          , @idRecepcionCuero
          , 8
        )
        
      insert into
        tb_inventarioCrudo
        (
          noPiezasActual
          , KgTotalActual
          , pesoXPieza
          , idRecepcionCuero
          , idTipoRecorte
        )
      
      values
        (
          @noPiezas2
          , @kg2
          , @kg2 / @noPiezas2
          , @idRecepcionCuero
          , 7
        )
    end
    
    else
    begin
      
      insert into
        tb_inventarioCrudo
        (
          noPiezasActual
          , KgTotalActual
          , pesoXPieza
          , idRecepcionCuero
          , idTipoRecorte
        )
        
      values
        (
          @noPiezas1
          , @kg1
          , @kg1 / @noPiezas1
          , @idRecepcionCuero
          , @idTipoRecorte
        )
    end
    
  end
go


