use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtFichaProdEli')
begin 
  drop
    procedure sp_obtFichaProdEli
end
go

create procedure sp_obtFichaProdEli
(
	@idFichaProdDet int
)
as begin

  declare
    @idFichaProd  int
    , @piezas       int
    , @piezasAct    int
    , @noRegistros  int
    , @contador     int
    , @validaElimina int
    
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
    
    select top(1)
      @idPartidaDetAux = idPartidaDet
      , @piezasAct = noPiezasAct
      , @piezas = noPiezas
    from
      @tb_partidaDet_temp
      
    if (@piezasAct <> @piezas)
    begin
      set
        @validaElimina = 1
        break   
    end
    
    else
    begin
      delete from
        @tb_partidaDet_temp
      where
        idPartidaDet = @idPartidaDetAux
    end
    
    set @contador = @contador + 1
    
    if (@contador = @noRegistros)
    begin
      set
        @validaElimina = 0
    end
  end
  
  select @validaElimina as validaElimina
 end
go