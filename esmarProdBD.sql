use master
go

if exists (select name from sys.databases WHERE name = 'esmarProd')
begin 
  drop
    database esmarProd 
end

create database esmarProd
go 

use esmarProd
go

create table tb_usuario(
  idUsuario int not null identity(1,1) primary key
  , usuario varchar(15)
  , contrasenia varchar(15)
  , nombre varchar(25)
  , tipo varchar(10)
  , constraint un_usuario unique(usuario)
);
go

create table tb_tipoMerma (
  idTipoMerma int not null identity(1,1) primary key
  , descripcion varchar(50)
  , constraint un_descripcionMerma unique(descripcion)
);
go

create table tb_configMerma (
  idConfigMerma int not null identity(1,1) primary key
  , idTipoMerma int not null foreign key references tb_tipoMerma(idTipoMerma)
  , porcMermaAcep float
  , fechaConfig datetime
);
go

create table tb_tipoCuero (
  idTipoCuero int not null identity(1,1) primary key
  , descripcion varchar(20)
  , constraint un_descripcionTipoCuero unique(descripcion)
);
go

create table tb_proveedor (
  idProveedor int not null identity(1,1) primary key
  , nombreProveedor varchar(100)
  , estatus int
  , constraint un_nombreProveedor unique(nombreProveedor) 
);
go

create table tb_rangoPesoCuero (
  idRangoPesoCuero int not null identity(1,1) primary key
  , rangoMin float
  , rangoMax float
  , fechaConfig datetime
);
go

create table tb_recepcionCuero (
  idRecepcionCuero int not null identity(1,1) primary key
  , idProveedor int not null foreign key references tb_proveedor(idProveedor)
  , noCamion int
  , idTipoCuero int not null foreign key references tb_tipoCuero(idTipoCuero)
  , idRangoPesoCuero int not null foreign key references tb_rangoPesoCuero(idRangoPesoCuero)
  , noPiezasLigero int
  , noPiezasPesado int
  , noTotalPiezas int
  , kgTotal float
  , precioXKilo float
  , costocamion float
  , mermaSal float
  , mermaHumedad float
  , mermaCachete float
  , mermaTarimas float
  , refParaMerma int
  , idMerSal int
  , idMerHum int
  , idMerCac int
  , idMerTar int
  , fechaEntrada date
);
go

create table tb_proceso (
  idProceso int not null identity(1,1) primary key
  , descripcion varchar(20)
  , constraint un_descripcionProceso unique(descripcion) 
);
go

create table tb_subProceso (
  idSubproceso int not null identity(1,1) primary key
  , idProceso int not null foreign key references tb_proceso(idProceso)
  , descripcion varchar(50)
  , constraint un_descripcionSubProceso unique(descripcion) 
);
go

create table tb_formXsubProc (
  idFormXSubProc int not null identity(1,1) primary key
  , idSubproceso int not null foreign key references tb_Subproceso(idSubProceso)
  , fechaCreacion datetime
);
go

create table tb_insumXproc (
  idInsumXProc int not null identity(1,1) primary key
  , idFormXSubProc int not null foreign key references tb_formXsubProc(idFormXSubProc)
  , clave varchar(10)
  , porcentaje float
  , idInsumo int
);
go

create table tb_inventarioCrudo (
  idInventarioCrudo int not null identity(1,1) primary key
  , noPiezasActual int not null
  , idRecepcionCuero int not null foreign key references tb_recepcionCuero(idRecepcionCuero)
);
go

create table tb_tipoRecorte (
  idTipoRecorte int not null identity(1,1) primary key
  , descripcion varchar (20)
);
go

create table tb_partida (
  idPartida int not null identity(1,1) primary key
  , noPartida int
  , noTotalPiezas int
  , fecha date
  , idProceso int not null foreign key references tb_proceso(idProceso)
);
go

create table tb_partidaDet (
  idPartidaDet int not null identity(1,1) primary key
  , noPiezas int
  , idPartida int not null foreign key references tb_partida(idPartida)
  , idRecepcionCuero int not null foreign key references tb_recepcionCuero(idRecepcionCuero)
  , idTipoRecorte int not null foreign key references tb_tipoRecorte(idTipoRecorte)
);
go

-- PROCEDURES 
create procedure sp_valUsulog 
  (
    @usuario varchar(15)
  )
  as begin
    select 
      * 
    from 
      tb_usuario 
    where 
      usuario = @usuario
  end
go

create procedure sp_obtProv
  as begin
    select 
      nombreProveedor, estatus
    from
      tb_proveedor
  end
go

create procedure sp_obtProvAct
  as begin
    select 
      idProveedor, nombreProveedor 
    from 
      tb_proveedor 
    where 
      estatus = 1
  end
go

create procedure sp_obtProvXnombre
  (
    @nombreProveedor varchar(100)
  )
  as begin
    select 
      *
    from
      tb_proveedor
    where
      nombreProveedor = @nombreProveedor
  end
go

create procedure sp_insProv
  (
    @nombreProveedor varchar(100)
    , @estatus int
  )
  as begin
    insert into
      tb_proveedor 
    values
      (@nombreProveedor,@estatus)
  end
go

create procedure sp_actProv
  (
    @nombreProveedor varchar(100)
    , @estatus int
    , @idProveedor int
  )
  as begin
    update
      tb_proveedor 
    set
      nombreProveedor = @nombreProveedor
      , estatus = @estatus
    where
      idProveedor = @idProveedor
  end
go

create procedure sp_obtEntReccuero 
  (
    @proveedor varchar(100)
    , @tipoCuero varchar(20)
    , @fecha varchar(10)
    , @fecha1 varchar(10)
  )
  as begin
    select 
      rc.idrecepcioncuero, p.nombreproveedor, tp.descripcion, rc.nocamion, rc.nototalpiezas, rc.kgtotal, rc.precioxkilo ,(rc.kgtotal*rc.precioxkilo) as costocamion, rc.fechaentrada
    from 
      tb_proveedor as p inner join tb_recepcioncuero as rc
      on p.idproveedor = rc.idproveedor
      inner join tb_tipocuero as tp
      on rc.idtipocuero = tp.idtipocuero
    where 
      p.nombreproveedor like @proveedor
      and tp.descripcion like @tipoCuero
      and rc.fechaentrada between @fecha and @fecha1
  end
go
                        
create procedure sp_agrFormSubProc 
  (
    @idSubProceso int
  )
  as begin
    declare @fechaCreacion datetime
    set @fechaCreacion = 
    (
      select getdate()
    )
    insert into 
      tb_formXsubProc 
    values 
      (
        @idSubproceso
        , @fechaCreacion
      )
  end
go

create procedure sp_agrInsumXProc 
  (
    @idSubProceso int
    , @clave varchar(10)
    , @porcentaje float
    , @idInsumo int
  )
  as begin
    declare @idFormXSubProc int
    set @idFormXSubProc = 
    (  
      select
        idFormXSubProc
      from 
        tb_formXsubProc
      where 
        fechaCreacion = 
        (
          select
            max(fechaCreacion)
          from 
            tb_formXsubProc
          where 
            idSubProceso = @idSubProceso
        )
    )
      
    insert into 
      tb_insumXproc 
    values 
      (
        @idFormXSubProc
        , @clave
        , @porcentaje
        , @idInsumo
      )
  end
go
    
create procedure sp_obtFormInsXSubProc 
  (
    @idSubProceso int
  )
  as begin
    declare 
      @idFormXSubProc int
    set
      @idFormXSubProc = 
      (
        select
          idFormXSubProc
        from 
          tb_formXsubProc
        where 
          fechaCreacion = 
          (
            select 
              max (fechaCreacion)
            from 
              tb_formXsubProc
            where 
              idSubproceso = @idSubProceso
          )
      )
    
    select
      *
    from 
      tb_insumXproc
    where 
      idFormXSubProc = @idFormXSubProc
  end
go

create procedure sp_obtProc
  as begin
    declare 
      @procInicio int
      , @procFin  int
    
    set @procInicio = 1
    set @procFin  = 7

    select
      *
    from 
      tb_proceso
    where
      idProceso not in (@procInicio, @procFin)
    order by
      idProceso
  end
go

create procedure sp_obtSubProc
  (
    @nombreproceso varchar(20)
  )
  as begin
    select
      sp.idSubProceso, sp.descripcion as 'subProceso', p.descripcion as 'proceso'
    from 
      tb_subProceso as sp inner join tb_proceso as p
      on sp.idProceso = p.idProceso
    where
      p.descripcion like @nombreproceso 
    order by
      sp.idProceso
  end
go

create procedure sp_obtSubProcXid
  (
    @idProceso int
  )
  as begin
    select
      *
    from 
      tb_subProceso
    where
      idProceso = @idProceso 
  end
go

create procedure sp_obtSubProcXdesc
  (
    @subProceso varchar(20)
  )
  as begin
    select
      *
    from 
      tb_subProceso
    where 
      descripcion = @subProceso
  end
go

create procedure sp_insSubProc
  (
    @subProceso varchar(20)
    , @proceso varchar(20)
  )
  as begin
    declare @idProceso int
    
    set @idProceso = 
    (
      select
        idProceso
      from 
        tb_proceso
      where 
        descripcion = @proceso
    )
    
    insert into 
      tb_subProceso
    values
      (@idProceso,@subProceso)
  end
go

create procedure sp_actSubProc
  (
    @subProceso varchar(20)
    , @proceso varchar(20)
    , @idSubProceso int
  )
  as begin
    declare @idProceso int
    
    set @idProceso = 
    (
      select
        idProceso
      from 
        tb_proceso
      where 
        descripcion = @proceso
    )
    
    update
      tb_subProceso
    set
      idProceso = @idProceso
      , descripcion = @subProceso
    where
      idSubProceso = @idSubProceso
  end
go

create procedure sp_calCostTot
(
  @sal float
  , @humedad float
  , @cachete float
  , @tarimas float
  , @kgTotales float
  , @precio float
  , @piezasTotales int
  , @refParaMerma int
)
as begin
  declare @salAcep float
  declare @humedadAcep float
  declare @cacheteAcep float
  declare @tarimasAcep float
  declare @salReal float
  declare @humedadReal float
  declare @cacheteReal float
  declare @tarimasReal float
  declare @salDiferencia float
  declare @HumedadDiferencia float
  declare @cacheteDiferencia float
  declare @tarimasDiferencia float
  declare @salDescontar float
  declare @humedadDescontar float
  declare @cacheteDescontar float
  declare @tarimasDescontar float
  declare @totalKgDescontar float
  declare @totalDescontar float
  declare @totalPagar float
  declare @humedadAcepCalc float
  declare @idConfigMermaSal int
  declare @idConfigMermaHumedad int
  declare @idConfigMermaCachete int
  declare @idConfigMermaTarimas int
  
  set @salAcep =
  (
    select
      porcMermaAcep
    from
      tb_configMerma
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
        from
          tb_configMerma
        where
          idTipoMerma = 1
      )
  )
  
  set @humedadAcep =
  (
    select
      porcMermaAcep
    from
      tb_configMerma
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
        from
          tb_configMerma
        where
          idTipoMerma = 2
      )
  )
  
  set @humedadAcepCalc = @humedadAcep * @kgTotales
  
  set @cacheteAcep =
  (
    select
      porcMermaAcep
    from
      tb_configMerma
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
        from
          tb_configMerma
        where
          idTipoMerma = 3
      )
  )
  
  set @tarimasAcep =
  (
    select
      porcMermaAcep
    from
      tb_configMerma
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
        from
          tb_configMerma
        where
          idTipoMerma = 4
      )
  )
  
  set @idConfigMermaSal =
  (
    select
      idConfigMerma
    from
      tb_configMerma
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
        from
          tb_configMerma
        where
          idTipoMerma = 1
      )
  )
  
  set @idConfigMermaHumedad =
  (
    select
      idConfigMerma
    from
      tb_configMerma
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
        from
          tb_configMerma
        where
          idTipoMerma = 2
      )
  )
  
  set @idConfigMermaCachete =
  (
    select
      idConfigMerma
    from
      tb_configMerma
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
        from
          tb_configMerma
        where
          idTipoMerma = 3
      )
  )
  
  set @idConfigMermaTarimas =
  (
    select
      idConfigMerma
    from
      tb_configMerma
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
        from
          tb_configMerma
        where
          idTipoMerma = 4
      )
  )
  
  if (@piezasTotales = 0)
  begin
    set @salReal = 0
  end
  else
  begin
    set @salReal = @sal/@piezasTotales
  end
  
  if (@refParaMerma = 0)
  begin
    set @humedadReal = 0
    set @cacheteReal = 0
  end
  else
  begin
    set @humedadReal = (@humedad/@refParaMerma)*@piezasTotales
    set @cacheteReal = @cachete/@refParaMerma
  end
  
  set @tarimasReal = @tarimas
  
  set @salDiferencia = @salReal-@salAcep
  set @humedadDiferencia = @humedadReal-@humedadAcepCalc
  set @cacheteDiferencia = @cacheteReal-@cacheteAcep
  set @tarimasDiferencia = @tarimasReal-@tarimasAcep
  
  set @salDescontar = @salDiferencia*@piezasTotales
  set @humedadDescontar = @humedadDiferencia
  set @cacheteDescontar = @cacheteDiferencia*@piezasTotales
  set @tarimasDescontar = @tarimasDiferencia
  
  set @totalDescontar = 0
  
  if (@salDescontar > 0)
  begin
    select @totalDescontar = @totalDescontar+@salDescontar
  end
  
  if (@humedadDescontar > 0)
  begin
    select @totalDescontar = @totalDescontar+@humedadDescontar
  end
  
  if (@cacheteDescontar > 0)
  begin
    select @totalDescontar = @totalDescontar+@cacheteDescontar
  end
  
  if (@tarimasDescontar > 0)
  begin
    select @totalDescontar = @totalDescontar+@tarimasDescontar
  end
  
  set @totalKgDescontar = @kgTotales-@totalDescontar
  set @totalPagar = @totalKgDescontar*@precio
  
  select
    @totalPagar as totalPagar
    , @salAcep as salAcep
    , @humedadAcepCalc as humedadAcepCalc
    , @cacheteAcep as cacheteAcep
    , @tarimasAcep as tarimasAcep
    , @salReal as salReal
    , @humedadReal as humedadReal
    , @cacheteReal as cacheteReal
    , @tarimasReal as tarimasReal
    , @salDiferencia as salDiferencia
    , @humedadDiferencia as humedadDiferencia
    , @cacheteDiferencia as cacheteDiferencia
    , @tarimasDiferencia as tarimasDiferencia
    , @salDescontar as salDescontar
    , @humedadDescontar as humedadDescontar
    , @cacheteDescontar as cacheteDescontar
    , @tarimasDescontar as tarimasDescontar
    , @totalDescontar as totalDescontar
    , @humedadAcep as humedadAcep
    , @idConfigMermaSal as idConfigMermaSal
    , @idConfigMermaHumedad as idConfigMermaHumedad
    , @idConfigMermaCachete as idConfigMermaCachete
    , @idConfigMermaTarimas as idConfigMermaTarimas
end
go

create procedure sp_obtNoCamion 
  (
    @idProveedor int
  )
  as begin
    declare @anioActual int
    declare @noCamion int
    
    set @anioActual = (
    Select
      year(getdate()))
      
    set @noCamion = (
    select
      max(noCamion)
    from
      tb_recepcionCuero
    where idProveedor = @idProveedor and year(fechaEntrada) =  @anioActual)
    
    if(@noCamion is null) begin
      set @noCamion = 1
    end
    
    else begin
      set @noCamion = @noCamion+1
    end
    
    select @noCamion as noCamion
  end
go

create procedure sp_obtRangoPesoCuero 
as begin
  select
    idRangoPesoCuero
    ,rangoMin
    ,rangoMax
  from
    tb_rangoPesoCuero
  where
    fechaConfig = 
    (
      select 
        max(fechaConfig)
      from
        tb_rangoPesoCuero
    )
end
go

create procedure sp_obtTipoCuero
  as begin
    select 
      idTipoCuero,descripcion 
    from 
      tb_tipoCuero
  end
go

create procedure sp_agrEntRecCuero
(
  @idProveedor int
  ,@noCamion int
  ,@idTipoCuero int
  ,@idRangoPesoCuero int
  ,@noPiezasLigero int
  ,@noPiezasPesado int
  ,@noTotalPiezas int
  ,@kgTotal float
  ,@precioXKilo float
  ,@mermaSal float
  ,@mermaHumedad float
  ,@mermaCachete float
  ,@mermaTarimas float
  ,@refParamerma int
  ,@idMerSal int
  ,@idMerHum int
  ,@idMerCac int
  ,@idMerTar int
)
as begin
  declare @fechaEntrada datetime
  declare @costoCamion float
  
  set @fechaEntrada =
    (
      select
        getdate()
    )
  
  set @costoCamion = @kgTotal*@precioXKilo
  
  insert into
    tb_recepcionCuero
  values
    (@idProveedor,@noCamion,@idTipoCuero,@idRangoPesoCuero,@noPiezasLigero,@noPiezasPesado,@noTotalPiezas,@kgTotal,
    @precioXKilo,@costoCamion,@mermaSal,@mermaHumedad,@mermaCachete,@mermaTarimas,@refParamerma,@idMerSal,@idMerHum,@idMerCac,@idMerTar,@fechaEntrada)
end
go

create procedure sp_obtConfMerma
as begin
  declare @salAcep float
  declare @humedadAcep float
  declare @cacheteAcep float
  declare @tarimasAcep float
  
  set @salAcep =
  (
    select
      porcMermaAcep
    from
      tb_configMerma
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
        from
          tb_configMerma
        where
          idTipoMerma = 1
      )
  )
  
  set @humedadAcep =
  (
    select
      porcMermaAcep
    from
      tb_configMerma
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
        from
          tb_configMerma
        where
          idTipoMerma = 2
      )
  )
  
  set @cacheteAcep =
  (
    select
      porcMermaAcep
    from
      tb_configMerma
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
        from
          tb_configMerma
        where
          idTipoMerma = 3
      )
  )
  
  set @tarimasAcep =
  (
    select
      porcMermaAcep
    from
      tb_configMerma
    where
      idConfigMerma =
      (
        select
          max(idConfigMerma)
        from
          tb_configMerma
        where
          idTipoMerma = 4
      )
  )
  
  select
    @salAcep as salAcep, @humedadAcep as humedadAcep, @cacheteAcep as cacheteAcep, @tarimasAcep as tarimasAcep
end
go

create procedure sp_agrConfMerma
(
  @idTipoMerma int
  ,@porcMermaAcep float
)
as begin
  declare @fechaConfig datetime
  set @fechaConfig =
    (
      select
        getdate()
    )
  
  insert into
    tb_configMerma
  values
    (@idTipoMerma,@porcMermaAcep,@fechaConfig)
end
go

create procedure sp_agrConfRanPesoCue
(
  @rangoMin int
  ,@rangoMax int
)
as begin
  declare @fechaConfig datetime
  set @fechaConfig =
    (
      select
        getdate()
    )
  
  insert into
    tb_rangoPesoCuero
  values
    (@rangoMin,@rangoMax,@fechaConfig)
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
  
  set @humedadAcepCalc = @humedadAcep*@kgTotal
  
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
    select @totalDescKg = @totalDescKg+@tarimasDesc
  end
  
  set @totalPagarKg = @kgTotal-@totalDescKg
  set @totalPagar = @totalPagarKg*@precio
  
  select
    p.nombreProveedor, rc.fechaEntrada, rc.noCamion, rc.kgTotal, rc.precioXKilo, rc.costoCamion,
    rpc.rangoMin, rpc.rangoMax, rc.noPiezasLigero, rc.noPiezasPesado, rc.noTotalPiezas,
    rc.mermaSal, rc.mermaHumedad, rc.mermaCachete, rc.mermaTarimas,
    @salAcep as salAcep, @humedadAcepCalc as humedadAcepCalc, @cacheteAcep as cacheteAcep, @tarimasAcep as tarimasAcep,
    @salReal as salReal, @humedadReal as humedadReal, @cacheteReal as cacheteReal, @tarimasReal as tarimasReal,
    @salDif as salDif, @humedadDif as humedadDif, @cacheteDif as cacheteDif, @tarimasDif as tarimasDif,
    @salDesc as salDesc, @humedadDesc as humedadDesc, @cacheteDesc as cacheteDesc, @tarimasDesc as tarimasDesc,
    @totalDescKg as totalDescKg, @kgTotal as pesoCamion, @totalPagarKg as totalPagarKg, @totalPagar as totalPagar,
    @refParaMerma as refParaMerma, @humedadAcep*100 as humedadAcep
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

create procedure sp_obtRecCueroDisp
as begin
  select
    p.nombreProveedor, rc.noCamion, tp.descripcion, ic.noPiezasActual, rc.kgTotal, rc.kgTotal/ic.noPiezasActual as PromKgPieza, rc.fechaEntrada, rc.idRecepcionCuero
  from
    tb_proveedor as p
  inner join
    tb_recepcionCuero as rc
  on
    p.idProveedor = rc.idProveedor
  inner join
    tb_inventarioCrudo as ic
  on
    ic.idRecepcionCuero = rc.idRecepcionCuero
  inner join
    tb_tipoCuero as tp
  on
    rc.idTipoCuero = tp.idTipoCuero
  where
    ic.noPiezasActual > 0
end
go

create procedure sp_obtListaPartida
as begin
  select
    rc.noCamion, p.nombreproveedor, ic.noPiezasActual, rc.kgTotal
  from
    tb_proveedor as p
  inner join
    tb_recepcionCuero as rc
  on
    p.idProveedor = rc.idProveedor
  inner join
    tb_inventarioCrudo as ic
  on
    rc.idRecepcionCuero = ic.idRecepcionCuero
end
go

create procedure sp_obtNoPartida
  as begin
    declare @anioActual int
    declare @noPartida int
    
    set @anioActual = (
    Select
      year(getdate()))
      
    set @noPartida = (
    select
      max(noPartida)
    from
      tb_partida
    where year(fecha) =  @anioActual)
    
    if(@noPartida is null) begin
      set @noPartida = 1
    end
    
    else begin
      set @noPartida = @noPartida+1
    end
    
    select @noPartida as noPartida
  end
  go
  
create procedure sp_agrPartida
(
  @noPartida int
  , @noTotalPiezas int
  , @idProceso int
)
as begin
  declare @fecha datetime
  
  set @fecha = 
  (
    select getdate()
  )
  
  insert into
    tb_partida
  values
    (@noPartida, @noTotalPiezas, @fecha, @idProceso)
end
go

create procedure sp_agrPartidaDetalle
(
  @noPiezas int
  , @idPartida int
  , @idTipoRecorte int
  , @proveedor varchar(20)
  , @noCamion int
  , @fecha date
)
as begin
  declare @idRecepcionCuero int
  
  set @idRecepcionCuero =
    (
      select
        idRecepcionCuero
      from
        tb_recepcionCuero
      where
        noCamion = @noCamion
      and
        fechaEntrada = @fecha
      and
        idProveedor =
        (
          select
            idProveedor
          from
            tb_proveedor
          where
            nombreProveedor = @proveedor
        )
    )
  
  insert into
    tb_partidaDet
  values
    (@noPiezas, @idPartida, @idRecepcionCuero, @idTipoRecorte)
end
go

create procedure sp_actInvCrudo
(
  @proveedor varchar(20)
  , @noCamion int
  , @fecha date
  , @piezasUtilizar int
)
as begin
  declare @idRecepcionCuero int
  
  set @idRecepcionCuero =
    (
      select
        idRecepcionCuero
      from
        tb_recepcionCuero
      where
        noCamion = @noCamion
      and
        fechaEntrada = @fecha
      and
        idProveedor =
        (
          select
            idProveedor
          from
            tb_proveedor
          where
            nombreProveedor = @proveedor
        )
    )
    
  update
    tb_inventarioCrudo
  set
    noPiezasActual = noPiezasActual-@piezasUtilizar
  where
    idRecepcionCuero = @idRecepcionCuero
end
go

-- TRIGGERS --
create trigger tr_insInvCueroCrudo
on tb_recepcionCuero
after insert
as 
begin
  -- set nocount on impide que se generen mensajes de texto 
  set nocount on;

  insert into 
    tb_inventarioCrudo (noPiezasActual, idRecepcionCuero)
    
  select 
    noTotalPiezas, idRecepcionCuero
  from 
    tb_recepcionCuero
  where
    idRecepcionCuero = 
    (
      select 
        max(idRecepcionCuero) 
      from 
        tb_recepcionCuero
    )
end
go

-- DATOS DE PRUEBAS --
insert into 
  tb_usuario ("usuario", "contrasenia", "nombre", "tipo") 
values 
  ('sis', '123', 'CESAR LUNA', 'SISTEMAS'), ('Mario','MarioDL96','Mario Luna','SISTEMAS');
  
insert into 
  tb_proveedor ("nombreProveedor", "estatus") 
values 
  ('GALAVIZ', '1'), ('FELIX PIÑON', '1');
  
insert into 
  tb_tipoMerma 
values 
  ('SAL'),('HUMEDAD'),('CACHETE'),('TARIMAS');

insert into 
  tb_configMerma 
values
  ('1', '0.3', '2018-05-20T22:40:03.000')
  , ('2', '0.01', '2018-05-20T22:40:03.000')
  , ('3', '0.033', '2018-05-20T22:40:03.000')
  , ('4', '0', '2018-05-20T22:40:03.000');
  
  
insert into 
  tb_proceso ("descripcion") 
values 
  ('INICIO'),('REMOJO'),('PELAMBRE'),('DESENCALADO'),('CURTIDO'),('ENGRASE'),('FIN');
  
insert into
  tb_subProceso ("idProceso", "descripcion") 
values 
  ('1', 'REMOJO 1'),('1', 'REMOJO 2')
  ,('2', 'PELAMBRE 1'),('2', 'PELAMBRE 2')
  ,('3', 'DESENCALADO 1'),('3', 'DESENCALADO 2')
  ,('4', 'CURTIDO 1'),('4', 'CURTIDO 2')
  ,('5', 'ENGRASE 1'),('5', 'ENGRASE 2');

insert into
  tb_formXsubProc ("idSubproceso", "fechaCreacion") 
values 
  ('1', '2018-05-11T22:49:27.000'),('1', '2018-05-12T22:50:27.000')
  ,('2', '2018-05-11T22:51:27.000'),('2', '2018-05-12T22:52:27.000')
  ,('3', '2018-05-11T22:53:27.000'),('3', '2018-05-12T22:54:27.000');
  
insert into
  tb_insumXproc ("idFormXSubProc", "clave", "porcentaje", "idInsumo") 
values 
  ('1','', '100', '1'),('1','1', '50', '2')
  ,('2','', '70', '2'),('2','1', '60', '3')
  ,('3','', '10', '3'),('3','1', '20', '1')
  ,('4','', '20', '3'),('4','1', '30', '1')
  ,('5','', '20', '3'),('5','1', '30', '1')
  ,('6','', '66', '3'),('6','1', '77', '1');

insert into 
  tb_tipoCuero
values
  ('SANGRE'),('SAL');

insert into
  tb_rangoPesoCuero
values
  ('20','100','2018-05-12T22:50:27.000');
  
insert into
  tb_tipoRecorte
values
  ('Entero');
