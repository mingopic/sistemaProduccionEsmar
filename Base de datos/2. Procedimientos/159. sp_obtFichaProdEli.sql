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
	@idPartidaDet int
)
as begin

  declare 
    @idFichaProd int
    , @piezas int
    , @piezasAct int
    
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
    idPartidaDet = @idPartidaDet
  
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
  
  select * from @tb_partidaDet_temp
  
/*  
  set
    @piezasAct =
    (
      select
        noPiezasAct
          
      from
        tb_partidaDet
             
      where
        idPartidaDet = @idPartidaDet
    )
    
  set
    @piezas =
    (
      select
        noPiezas
          
      from
        tb_partidaDet
             
      where
        idPartidaDet = @idPartidaDet
    )
    
    if (@piezasAct = @piezas)
    begin
      select
        0 as validaElimina
    end
    
    else
    begin
      select
        1 as validaElimina
    end
  */
end
go