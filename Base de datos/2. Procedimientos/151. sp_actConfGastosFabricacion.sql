use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actConfGastosFabricacion')
begin 
  drop
    procedure sp_actConfGastosFabricacion
end
go

create procedure sp_actConfGastosFabricacion
(
	@idConfGastosFabricacion int
	, @costo                float
)
as begin
  declare @porcEntero         float
  , @porcDelSillero   float
  , @porcCrupSillero  float
  , @porcLados        float
  , @porcCentroCas    float
  , @porcCentroQue    float
  , @porcDelSuela     float
  , @porcCentro       float
  , @costoEntero      float
  , @costoDelSillero  float
  , @costoCrupSillero float
  , @costoLados       float
  , @costoCentroCas   float
  , @costoCentroQue   float
  , @costoDelSuela    float
  , @costoCentro      float
  
  set @porcEntero =
  (
    select
      porcentaje
    from
      tb_confPrecioCuero
    where
      idTipoRecorte = 1
  )
  
  set @porcDelSillero =
  (
    select
      porcentaje
    from
      tb_confPrecioCuero
    where
      idTipoRecorte = 2
  )
  
  set @porcCrupSillero =
  (
    select
      porcentaje
    from
      tb_confPrecioCuero
    where
      idTipoRecorte = 3
  )
  
  set @porcLados =
  (
    select
      porcentaje
    from
      tb_confPrecioCuero
    where
      idTipoRecorte = 4
  )
  
  set @porcCentroCas =
  (
    select
      porcentaje
    from
      tb_confPrecioCuero
    where
      idTipoRecorte = 5
  )
  
  set @porcCentroQue =
  (
    select
      porcentaje
    from
      tb_confPrecioCuero
    where
      idTipoRecorte = 6
  )
  
  set @porcDelSuela =
  (
    select
      porcentaje
    from
      tb_confPrecioCuero
    where
      idTipoRecorte = 7
  )
  
  set @porcCentro =
  (
    select
      porcentaje
    from
      tb_confPrecioCuero
    where
      idTipoRecorte = 8
  )
  
  set @costoEntero = @costo * @porcEntero
  set @costoDelSillero = @costo * @porcDelSillero
  set @costoCrupSillero = @costo * @porcCrupSillero
  set @costoLados = @costo * @porcLados
  set @costoCentroCas = @costo * @porcCentroCas
  set @costoCentroQue = @costo * @porcCentroQue
  set @costoDelSuela = @costo * @porcDelSuela
  set @costoCentro = @costo * @porcCentro
    
  insert into
    tb_confGastosFabricacion
    (
      idTipoRecorte
      , costo
      , fecha
    )
    
  values
    (
      1
      , @costoEntero
      , getdate()
    )
    ,
    (
      2
      , @costoDelSillero
      , getdate()
    )
    ,
    (
      3
      , @costoCrupSillero
      , getdate()
    )
    ,
    (
      4
      , @costoLados
      , getdate()
    )
    ,
    (
      5
      , @costoCentroCas
      , getdate()
    )
    ,
    (
      6
      , @costoCentroQue
      , getdate()
    )
    ,
    (
      7
      , @costoDelSuela
      , getdate()
    )
    ,
    (
      8
      , @costoCentro
      , getdate()
    )
end