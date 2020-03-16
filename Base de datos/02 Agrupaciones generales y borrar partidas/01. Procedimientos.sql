
use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_reiniciarPartida')
begin 
  drop
    procedure sp_reiniciarPartida
end
go

create procedure sp_reiniciarPartida 
  (
    @idPartida int
  )
  as begin
    /*
    =================================================================================================================================
      #Id  Autor     Fecha        Description
    ---------------------------------------------------------------------------------------------------------------------------------
      01   DLuna     2020/03/16   Creación
    =================================================================================================================================
    */ 
    
    declare 
      @continua  bit
      , @mensaje varchar(300)
      , @id      int
      
    declare
      @tbfichaProdDet table
      (
        idFichaProdDet int
        , idFichaProd  int
        , procesado    bit
      )
      
    declare
      @tbfichaProd table
      (
        idFichaProd int
        , procesado bit
      )
    
    declare
      @tbInsumosFichaProd table
      (
        idInsumoFichaProd int
        , procesado       bit
      )
    
    declare
      @tbpartidaDet table
      (
        idPartidaDet        int
        , noPiezas          int
        , idInventarioCrudo int
        , procedenciaCrudo  int
        , procesado         bit
      )
    
    set 
      @continua = 1
    
    begin try
      
      begin TRAN
      
      if exists
      (
        select
          1
        from
          dbo.tb_invCross
        where
          idPartida = @idPartida
      )
      begin
        select
          @continua = 0
          , @mensaje = 'Ya se ha mandado piezas de la partida a inventarios'
      end
      
      if (@continua = 1)
      begin
        
        -- llenar variale de tabla @tbfichaProdDet
        insert into
          @tbfichaProdDet
        select
          fpd.idFichaProdDet
          , fpd.idFichaProd
          , 0
        from
          dbo.tb_fichaProdDet fpd
          inner join
            dbo.tb_partidaDet pd
          on
            pd.idPartidaDet = fpd.idPartidaDet
        where
          pd.idPartida = @idPartida
        
        -- llenar variale de tabla @tbfichaProd
        insert into
          @tbfichaProd
        select
          distinct(idFichaProd)
          , 0
        from
          @tbfichaProdDet

        -- llenar variale de tabla @tbInsumosFichaProd
        insert into
          @tbInsumosFichaProd
        select
          ifp.idInsumoFichaProd
          , 0
        from
          tb_InsumosFichaProd ifp
          inner join
            @tbfichaProd fp
          on
            fp.idFichaProd = ifp.idFichaProd
        
        -- llenar variale de tabla @tbpartidaDet
        insert into
          @tbpartidaDet
        select
          idPartidaDet
          , noPiezas
          , idInventarioCrudo
          , procedenciaCrudo
          , 0
        from
          tb_partidaDet
        where
          idPartida = @idPartida
        
        -- Borrar datos de la tabla tb_fichaProdDet      
        while (select count(*) from @tbfichaProdDet where procesado = 0) > 0
        begin
        
            select 
              top 1 @id = idFichaProdDet 
            from 
              @tbfichaProdDet 
            where 
              procesado = 0

            delete from 
              tb_fichaProdDet 
            where
              idFichaProdDet = @id
            
            update
              @tbfichaProdDet
            set
              procesado = 1
            where
              idFichaProdDet = @id
        end
        
        -- Borrar datos de las tablas tb_InsumosFichaProd y tb_InsumosFichaProdDet
        while (select count(*) from @tbInsumosFichaProd where procesado = 0) > 0
        begin
        
            select 
              top 1 @id = idInsumoFichaProd 
            from 
              @tbInsumosFichaProd 
            where 
              procesado = 0

            delete from 
              tb_InsumosFichaProdDet 
            where
              idInsumoFichaProd = @id
              
            delete from 
              tb_InsumosFichaProd
            where
              idInsumoFichaProd = @id
            
            update
              @tbInsumosFichaProd
            set
              procesado = 1
            where
              idInsumoFichaProd = @id
        end
        
        -- Borrar datos de la tabla tb_fichaProd
        while (select count(*) from @tbfichaProd where procesado = 0) > 0
        begin
        
            select 
              top 1 @id = idFichaProd 
            from 
              @tbfichaProd 
            where 
              procesado = 0

            delete from 
              tb_fichaProd
            where
              idFichaProd = @id
            
            update
              @tbfichaProd
            set
              procesado = 1
            where
              idFichaProd = @id
        end
        
        declare
          @noPiezas            int
          , @procedenciaCrudo  int
          , @idInventarioCrudo int
              
        -- Borrar datos de las tablas tb_bajasPartidaDet, tb_partidaDetAux, tb_partidaDet
        while (select count(*) from @tbpartidaDet where procesado = 0) > 0
        begin
            
            select top 1 
              @id = idPartidaDet
              , @noPiezas = noPiezas
              , @procedenciaCrudo = procedenciaCrudo
              , @idInventarioCrudo = idInventarioCrudo
            from 
              @tbpartidaDet 
            where 
              procesado = 0

            delete from 
              tb_bajasPartidaDet
            where
              idPartidaDet = @id
            
            delete from 
              tb_partidaDetAux
            where
              idPartidaDet = @id
            
            if (@procedenciaCrudo = 1)
            begin
              
              declare
                @pesoXPieza float
              
              select
                @pesoXPieza = pesoXPieza  
              from 
                tb_inventarioCrudo
              where 
                idInventarioCrudo = @idInventarioCrudo
              
              update
                tb_inventarioCrudo
              set
                noPiezasActual = @noPiezas
                , kgTotalActual = (@pesoXPieza * @noPiezas)
              where
                idInventarioCrudo = @idInventarioCrudo
            end
            
            delete from 
              tb_partidaDet
            where
              idPartidaDet = @id
            
            update
              @tbpartidaDet
            set
              procesado = 1
            where
              idPartidaDet = @id
        end
        
        delete
          tb_garrasPartida
        where
          idPartida = @idPartida
          
        delete
          tb_partida
        where
          idPartida = @idPartida
      
      end
      
      commit TRAN
      
      if (@continua = 1)
      begin
      
        select
          [Procesado] = @continua
          , [Mensaje] = 'IdPartida eliminada correctamente'
      end
      
      else begin
        select
          [Procesado] = @continua
          , [Mensaje] = @mensaje
      end
    end try
    
    begin catch
      if exists(select 1 from sys.sysprocesses where open_tran = 1 and spid = @@SPID)
      begin
        rollback
      end
      
      select
          [Procesado] = 0
          , [Mensaje] = ERROR_MESSAGE()
          
    end catch
    
  end
go