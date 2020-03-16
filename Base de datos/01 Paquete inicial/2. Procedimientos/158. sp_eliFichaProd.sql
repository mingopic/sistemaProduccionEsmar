use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_eliFichaProd')
begin 
  drop
    procedure sp_eliFichaProd
end
go

create procedure sp_eliFichaProd
(
  @idFichaProdDet int
)
as begin
  
  declare 
    @idFichaProd           int
    , @idRecortePartidaDet int
    , @piezasAct           int
    , @noRegistros         int
    , @contador            int
  
  declare
    @tb_partidaDet_temp table 
    (
      idPartidaDet          int
      , noPiezas            int
      , noPiezasAct         int
      , idPartida           int
      , idRecepcionCuero    int
      , idTipoRecorte       int
      , idProceso           int
      , idInventarioCrudo   int
      , procedenciaCrudo    int -- 1 si, 0 no
      , idRecortePartidaDet int -- 0 cuándo no es una pieza recortada en el módulo de fichas de producción
    )
  
  -- Obtener la ficha a la que pertenece la partidaDet
  select
    @idFichaProd = idFichaProd
    
  from
    tb_fichaProdDet
    
  where
    idFichaProdDet = @idFichaProdDet
  
  -- insertar en la tabla temporal todas las partidasDet pertenecientes a la ficha de producción que se quiere eliminar   
  insert into
    @tb_partidaDet_temp
    
  select
    pd.*
    
  from
    tb_partidaDet pd
    
    inner join
      tb_fichaProdDet fpd
    on
      fpd.idPartidaDet = pd.idPartidaDet
      and fpd.idFichaProd = @idFichaProd
  
  --Se inicia ciclo
  set @contador = 0;
  
  select
    @noRegistros = count(1)
  from
    @tb_partidaDet_temp
    
    
  while (@contador < @noRegistros)
  begin
    declare @idPartidaDetAux int
    
    -- Se obtiene los datos de la partida a eliminar y actualizar piezas
    select top 1
      @idPartidaDetAux = idPartidaDet
      , @piezasAct = noPiezasAct
      , @idRecortePartidaDet = idRecortePartidaDet
    from
      @tb_partidaDet_temp
    
    -- Se regresan piezas eliminadas  
    update 
      tb_partidaDet
    set
      noPiezasAct = noPiezasAct + @piezasAct
    where
      idPartidaDet = @idRecortePartidaDet
      
    -- Se elimina insumosFichaProdDet
    delete from
      tb_InsumosFichaProdDet
    where
      idInsumoFichaProd = 
      (
        select
          idInsumoFichaProd
        from
          tb_InsumosFichaProd
        where
          idFichaProd = @idFichaProd
      )
    
    -- Se elimina insumosFichaProd
    delete from
      tb_InsumosFichaProd
    where
      idFichaProd = @idFichaProd
    
    -- Se elimina fichaProdDet
    delete from
      tb_fichaProdDet
    where
      idPartidaDet = @idPartidaDetAux
    
    -- Se elimina partidaDet
    delete from
      tb_partidaDet
    where
      idPartidaDet = @idPartidaDetAux
    
    -- Se elimina registro de la tabla temporal
    delete from
      @tb_partidaDet_temp
    where
      idPartidaDet = @idPartidaDetAux
      
    set @contador = @contador + 1
    
    -- Si se recorren todos los registros de la tabla temporal se elimina la fichaProd
    if (@contador = @noRegistros)
    begin
      delete from
        tb_fichaProd
      where
        idFichaProd = @idFichaProd
    end
  end    
end
go