use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insCuePesado')
begin 
  drop
    procedure sp_insCuePesado
end
go

create procedure sp_insCuePesado
(
  @idInventario         int
  , @noPartida        int
  , @tipoRecorte      varchar(100)
  , @calibre          varchar(100)
  , @seleccion        varchar(100)
  , @kg               float
  , @noPiezas         int
  , @noPiezasActuales int
  , @descripcion      varchar(100)
)
as begin
  
  declare
    @idTipoRecorte  int
    , @idCalibre    int
    , @idSeleccion  int
    
    set
      @idTipoRecorte =
      (  select
          idTipoRecorte
        from
          tb_tipoRecorte
        where
          descripcion = @tipoRecorte
      )
      
      set
      @idCalibre =
      (  select
          idCalibre
        from
          tb_calibre
        where
          descripcion = @calibre
      )
      
      set
      @idSeleccion =
      (  
        select
          idSeleccion
        from
          tb_seleccion
        where
          descripcion = @seleccion
      )
  
  insert into
    tb_CueroPesado
    
    values
    (
      @idInventario
      , @noPartida
      , @idTipoRecorte
      , @idCalibre
      , @idSeleccion
      , @kg
      , @noPiezas
      , @noPiezasActuales
      , @descripcion
      , getdate()
    )
end