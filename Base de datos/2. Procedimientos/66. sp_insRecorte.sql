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
      @idRecepcionCuero int
      
    update
      tb_partidaDet
    
    set
      noPiezasAct = noPiezasAct - @noPiezasAct
    
    where
      idPartidaDet = @idPartidaDet
    
    set
      @idRecepcionCuero = 
        (
          select
            idRecepcionCuero
          from
            tb_partidaDet
          where
            idPartidaDet = @idPartidaDet
        )
      
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
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , 2
          , @idProceso
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
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , 3
          , @idProceso
        )
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
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , 5
          , @idProceso
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
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , 7
          , @idProceso
        )
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
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , 6
          , @idProceso
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
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , 7
          , @idProceso
        )
    end
    
    else
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
        )
        
      values
        (
          @noPiezas
          , @noPiezas
          , @idPartida
          , @idRecepcionCuero
          , @idTipoRecorte
          , @idProceso 
        )
    end
    
  end
go


