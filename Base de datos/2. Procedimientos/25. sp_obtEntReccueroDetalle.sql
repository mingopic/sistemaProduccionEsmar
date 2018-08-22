use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtEntReccueroDetalle')
begin 
  drop
    procedure sp_obtEntReccueroDetalle
end
go

create procedure sp_obtEntReccueroDetalle
(
  @idRecepcionCuero int
)
as begin
  declare @sal float
  declare @humedad float
  declare @cachete float
  declare @tarimas float
  declare @salAcep float
  declare @humedadAcep float
  declare @humedadAcepCalc float
  declare @cacheteAcep float
  declare @tarimasAcep float
  declare @salReal float
  declare @humedadReal float
  declare @cacheteReal float
  declare @tarimasReal float
  declare @salDif float
  declare @humedadDif float
  declare @cacheteDif float
  declare @tarimasDif float
  declare @salDesc float
  declare @humedadDesc float
  declare @cacheteDesc float
  declare @tarimasDesc float
  declare @noTotalPiezas int
  declare @refParaMerma int
  declare @totalDescKg float
  declare @totalPagarKg float
  declare @totalPagar float
  declare @precio float
  declare @kgTotal float
  declare @kgTotalesConTarimas  float
  
  set @sal =
  (
    select
      mermaSal
    from
      tb_recepcionCuero
    where
      idRecepcionCuero = @idRecepcionCuero
  )
  
  set @humedad =
  (
    select
      mermaHumedad
    from
      tb_recepcionCuero
    where
      idRecepcionCuero = @idRecepcionCuero
  )
  
  set @cachete =
  (
    select
      mermaCachete
    from
      tb_recepcionCuero
    where
      idRecepcionCuero = @idRecepcionCuero
  )
  
  set @tarimas =
  (
    select
      mermaTarimas
    from
      tb_recepcionCuero
    where
      idRecepcionCuero = @idRecepcionCuero
  )
  
  set @kgTotal =
  (
    select
      kgTotal
    from
      tb_recepcionCuero
    where
      idRecepcionCuero = @idRecepcionCuero
  )
  
  set @kgTotalesConTarimas = @kgTotal
  
  set @salAcep =
  (
    select
      cm.porcMermaAcep
    from
      tb_configMerma as cm
    inner join
      tb_recepcionCuero as rc
    on
      cm.idConfigMerma = rc.idMerSal
    where
      idRecepcionCuero = @idRecepcionCuero
  )
  
  set @humedadAcep =
  (
    select
      cm.porcMermaAcep
    from
      tb_configMerma as cm
    inner join
      tb_recepcionCuero as rc
    on
      cm.idConfigMerma = rc.idMerHum
    where
      idRecepcionCuero = @idRecepcionCuero
  )
  
  if (@tarimas = 0)
  begin
    set @humedadAcepCalc = @humedadAcep*@kgTotal
  end
  
  else
  begin
    set @humedadAcepCalc = @humedadAcep*(@kgTotal+@tarimas)
  end
  
  set @cacheteAcep =
  (
    select
      cm.porcMermaAcep
    from
      tb_configMerma as cm
    inner join
      tb_recepcionCuero as rc
    on
      cm.idConfigMerma = rc.idMerCac
    where
      idRecepcionCuero = @idRecepcionCuero
  )
  
  set @tarimasAcep =
  (
    select
      cm.porcMermaAcep
    from
      tb_configMerma as cm
    inner join
      tb_recepcionCuero as rc
    on
      cm.idConfigMerma = rc.idMerTar
    where
      idRecepcionCuero = @idRecepcionCuero
  )
  
  set @noTotalPiezas =
  (
    select
      noTotalPiezas
    from
      tb_recepcionCuero
    where
      idRecepcionCuero = @idRecepcionCuero
  )
  
  set @refParaMerma =
  (
    select
      refParaMerma
    from
      tb_recepcionCuero
    where
      idRecepcionCuero = @idRecepcionCuero
  )
  
  set @precio =
  (
    select
      precioXKilo
    from
      tb_recepcionCuero
    where
      idRecepcionCuero = @idRecepcionCuero
  )
  
  if (@noTotalPiezas = 0)
  begin
    set @salReal = 0
  end
  else
  begin
    set @salReal = @sal/@noTotalPiezas
  end
  
  if (@refParaMerma = 0)
  begin
    set @humedadReal = 0
    set @cacheteReal = 0
  end
  else
  begin
    set @humedadReal = (@humedad/@refParaMerma)*@nototalpiezas
    set @cacheteReal = @cachete/@refParaMerma
  end
  
  set @tarimasReal = @tarimas
  
  set @salDif = @salReal-@salAcep
  set @humedadDif = @humedadReal-@humedadAcepCalc
  set @cacheteDif = @cacheteReal-@cacheteAcep
  set @tarimasDif = @tarimasReal-@tarimasAcep
  
  set @salDesc = @salDif*@noTotalPiezas
  set @humedadDesc = @humedadDif
  set @cacheteDesc = @cacheteDif*@noTotalPiezas
  set @tarimasDesc = @tarimasDif
  
  set @totalDescKg = 0
  
  if (@salDesc > 0)
  begin
    select @totalDescKg = @totalDescKg+@salDesc
  end
  
  if (@humedadDesc > 0)
  begin
    select @totalDescKg = @totalDescKg+@humedadDesc
  end
  
  if (@cacheteDesc > 0)
  begin
    select @totalDescKg = @totalDescKg+@cacheteDesc
  end
  
  if (@tarimasDesc > 0)
  begin
    select @kgTotalesConTarimas = @kgTotal+@tarimasDesc
  end
  
  set @totalPagarKg = @kgTotalesConTarimas-@totalDescKg
  set @totalPagar = @totalPagarKg*@precio
  
  select
    p.nombreProveedor
    , rc.fechaEntrada
    , rc.noCamion
    , rc.kgTotal
    , rc.precioXKilo
    , rc.costoCamion
    , rpc.rangoMin
    , rpc.rangoMax
    , rc.noPiezasLigero
    , rc.noPiezasPesado
    , rc.noTotalPiezas
    , rc.mermaSal
    , rc.mermaHumedad
    , rc.mermaCachete
    , rc.mermaTarimas
    , @salAcep as salAcep
    , @humedadAcepCalc as humedadAcepCalc
    , @cacheteAcep as cacheteAcep
    , @tarimasAcep as tarimasAcep
    , @salReal as salReal
    , @humedadReal as humedadReal
    , @cacheteReal as cacheteReal
    , @tarimasReal as tarimasReal
    , @salDif as salDif
    , @humedadDif as humedadDif
    , @cacheteDif as cacheteDif
    , @tarimasDif as tarimasDif
    , @salDesc as salDesc
    , @humedadDesc as humedadDesc
    , @cacheteDesc as cacheteDesc
    , @tarimasDesc as tarimasDesc
    , @totalDescKg as totalDescKg
    , @kgTotalesConTarimas as pesoCamion
    , @totalPagarKg as totalPagarKg
    , @totalPagar as totalPagar
    , @refParaMerma as refParaMerma
    , @humedadAcep * 100 as humedadAcep
    
  from
    tb_proveedor as p
    
    inner join
      tb_recepcionCuero as rc
    on
      p.idProveedor = rc.idProveedor
      
    inner join
      tb_rangoPesoCuero as rpc
    on
      rc.idRangoPesoCuero = rpc.idRangoPesoCuero
      
  where
    rc.idRecepcionCuero = @idRecepcionCuero
end
go