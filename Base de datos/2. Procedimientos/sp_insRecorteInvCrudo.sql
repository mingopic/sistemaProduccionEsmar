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
    
    update
      tb_inventarioCrudo
    
    set
      noPiezasActual = noPiezasActual - @noPiezasOriginal
      , KgTotalActual = KgTotalActual - @kg1 - @kg2
    
    where
      idInventarioCrudo = @idInventarioCrudo
      
    if @idTipoRecorte = 0
    begin
    
      insert into
        tb_inventarioCrudo
        (
          noPiezasActual
          , KgTotalActual
          , idRecepcionCuero
          , idTipoRecorte
        )
        
      values
        (
          @noPiezas1
          , @kg1
          , @idRecepcionCuero
          , 2
        )
        
      insert into
        tb_inventarioCrudo
        (
          noPiezasActual
          , KgTotalActual
          , idRecepcionCuero
          , idTipoRecorte
        )
      
      values
        (
          @noPiezas2
          , @kg2
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
          , idRecepcionCuero
          , idTipoRecorte
        )
        
      values
        (
          @noPiezas1
          , @kg1
          , @idRecepcionCuero
          , 5
        )
        
      insert into
        tb_inventarioCrudo
        (
          noPiezasActual
          , KgTotalActual
          , idRecepcionCuero
          , idTipoRecorte
        )
      
      values
        (
          @noPiezas2
          , @kg2
          , @idRecepcionCuero
          , 7
        )
    end
    
    else if @idTipoRecorte = 2
    begin
    
      insert into
        tb_inventarioCrudo
        (
          noPiezasActual
          , KgTotalActual
          , idRecepcionCuero
          , idTipoRecorte
        )
        
      values
        (
          @noPiezas1
          , @kg1
          , @idRecepcionCuero
          , 6
        )
        
      insert into
        tb_inventarioCrudo
        (
          noPiezasActual
          , KgTotalActual
          , idRecepcionCuero
          , idTipoRecorte
        )
      
      values
        (
          @noPiezas2
          , @kg2
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
          , idRecepcionCuero
          , idTipoRecorte
        )
        
      values
        (
          @noPiezas1
          , @kg1
          , @idRecepcionCuero
          , @idTipoRecorte
        )
    end
    
  end
go


