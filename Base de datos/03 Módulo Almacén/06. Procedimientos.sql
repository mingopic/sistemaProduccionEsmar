
use esmarProd
go

if object_id('dbo.sp_reiniciarPartida') is not null
begin
	drop procedure dbo.sp_reiniciarPartida
end
go

if object_id('dbo.Usp_MaterialGetAll') is not null
begin
	drop procedure dbo.Usp_MaterialGetAll
end
go

if object_id('dbo.Usp_MaterialCreate') is not null
begin
	drop procedure dbo.Usp_MaterialCreate
end
go

if object_id('dbo.Usp_CatalogoDetGetByCatId') is not null
begin
	drop procedure dbo.Usp_CatalogoDetGetByCatId
end
go

if object_id('dbo.Usp_EntradaMaterialCreate') is not null
begin
	drop procedure dbo.Usp_EntradaMaterialCreate
end
go

if object_id('dbo.Usp_EntradaMaterialCreate') is not null
begin
	drop procedure dbo.Usp_EntradaMaterialCreate
end
go

if object_id('dbo.Usp_SalidaMaterialCreate') is not null
begin
	drop procedure dbo.Usp_SalidaMaterialCreate
end
go

if object_id('dbo.Usp_MaterialGetCollectionByCatDetTipoMaterialId') is not null
begin
	drop procedure dbo.Usp_MaterialGetCollectionByCatDetTipoMaterialId
end
go

if object_id('dbo.Usp_InsumosFichaProdUpdateEstatusSurtido') is not null
begin
	drop procedure dbo.Usp_InsumosFichaProdUpdateEstatusSurtido
end
go

if object_id('dbo.sp_agrFormSubProc') is not null
begin
	drop procedure dbo.sp_agrFormSubProc
end
go

if object_id('dbo.sp_agrInsumXProc') is not null
begin
	drop procedure dbo.sp_agrInsumXProc
end
go

if object_id('dbo.sp_obtFormInsXSubProc') is not null
begin
	drop procedure dbo.sp_obtFormInsXSubProc
end
go

if object_id('dbo.sp_InsInsumosFichaProd') is not null
begin
	drop procedure dbo.sp_InsInsumosFichaProd
end
go

if object_id('dbo.sp_InsInsumosFichaProdDet') is not null
begin
	drop procedure dbo.sp_InsInsumosFichaProdDet
end
go

if object_id('dbo.Usp_MaterialUpdate') is not null
begin
	drop procedure dbo.Usp_MaterialUpdate
end
go

if object_id('dbo.Usp_InsumosFichaProdValidarSurtido') is not null
begin
	drop procedure dbo.Usp_InsumosFichaProdValidarSurtido
end
go

if object_id('dbo.Usp_MaterialGetCollectionByIdInsumoFichaProd') is not null
begin
	drop procedure dbo.Usp_MaterialGetCollectionByIdInsumoFichaProd
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

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

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_MaterialGetAll 
(
  @Codigo                   varchar(10) = ''
  , @CatDetTipoMaterialId   int = 0
  , @CatDetClasificacionId  int = 0
)
  as begin
    /*
    =================================================================================================================================
      #Id  Autor     Fecha        Description
    ---------------------------------------------------------------------------------------------------------------------------------
      01   DLuna     2020/04/10   Creación
    =================================================================================================================================
    */ 
    
    select
      MaterialId
      , Codigo
      , m.Descripcion
      , Existencia
      , m.idUnidadMedida
      , Precio
      , m.idTipoMoneda
      , CatDetTipoMaterialId
      , CatDetClasificacionId
      , CatDetEstatusId
      , m.FechaUltimaAct
      , [UnidadMedida] = um.descripcion
      , [TipoMoneda] = tm.descripcion
      , [TipoMaterial] = cdT.Nombre
      , [Clasificacion] = cdC.Nombre
      , [Estatus] = cdE.Nombre
    
    from
      dbo.Tb_Material m
    
      inner join
        tb_unidadMedida um
      on
        um.idUnidadMedida = m.idUnidadMedida
      
      inner join
        tb_tipoMoneda tm
      on
        tm.idTipoMoneda = m.idTipoMoneda
      
      inner join
        Tb_CatalogoDet cdT
      on
        cdT.CatDetId = m.CatDetTipoMaterialId
        
      inner join
        Tb_CatalogoDet cdC
      on
        cdC.CatDetId = m.CatDetClasificacionId
        
      inner join
        Tb_CatalogoDet cdE
      on
        cdE.CatDetId = m.CatDetEstatusId
    
    where
      m.Codigo =
        case
          when @Codigo != ''
           then @Codigo
           else m.Codigo
        end
      and m.CatDetTipoMaterialId = 
        case
          when @CatDetTipoMaterialId > 0
           then @CatDetTipoMaterialId
           else m.CatDetTipoMaterialId
        end
      and m.CatDetClasificacionId = 
        case
          when @CatDetClasificacionId > 0
           then @CatDetClasificacionId
           else m.CatDetClasificacionId
        end
        
    order by
      m.Descripcion asc
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_MaterialCreate 
(
  @Codigo                  varchar(10)
  , @Descripcion           varchar(100)
  , @Existencia            float
  , @idUnidadMedida        int
  , @Precio                float
  , @idTipoMoneda          int
  , @CatDetTipoMaterialId  int
  , @CatDetClasificacionId int
  , @Respuesta             int = -1 output
  , @Mensaje               varchar(60) = '' output
)
  as begin
    /*
    =================================================================================================================================
      #Id  Autor     Fecha        Description
    ---------------------------------------------------------------------------------------------------------------------------------
      01   DLuna     2020/04/12   Creación
    =================================================================================================================================
    */ 
    
    declare 
      @EstatusActivo  int
      , @Guardar      int
      
    select
      @EstatusActivo = 25
      , @Guardar = 1
      
    if exists ( select Codigo from dbo.Tb_Material where Codigo = @Codigo)
    begin
      select 
        @Guardar = 0 
        , @Mensaje = 'Ya existe el código ingresado para otro material'
        , @Respuesta = 0
    end
    
    if exists ( select Descripcion from dbo.Tb_Material where Descripcion = @Descripcion)
    begin
      select 
        @Guardar = 0 
        , @Mensaje = 'Ya existe material con la descripcion ingresada'
        , @Respuesta = 0
    end
    
    if (@Guardar = 1)
    begin
    
      insert into dbo.Tb_Material
      (
        Codigo
        , Descripcion
        , Existencia
        , idUnidadMedida
        , Precio
        , idTipoMoneda
        , CatDetTipoMaterialId
        , CatDetClasificacionId
        , CatDetEstatusId
      )
      values
      (
        @Codigo
        , @Descripcion
        , @Existencia
        , @idUnidadMedida
        , @Precio
        , @idTipoMoneda
        , @CatDetTipoMaterialId
        , @CatDetClasificacionId
        , @EstatusActivo
      )
      
      select
        @Respuesta = SCOPE_IDENTITY()
        , @Mensaje = 'Material Guardado correctamente'
    end
    
    select 
      @Respuesta
      , @Mensaje
    
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_CatalogoDetGetByCatId 
(
  @CatId int
)
  as begin
    /*
    =================================================================================================================================
      #Id  Autor     Fecha        Description
    ---------------------------------------------------------------------------------------------------------------------------------
      01   DLuna     2020/04/12   Creación
    =================================================================================================================================
    */ 
      
    select
      CatDetId
      , Nombre
      , Abreviacion
      , OrdenVisualizacion
      , Auxiliar
      , FechaUltimaAct
    
    from
      dbo.Tb_CatalogoDet
    
    where
      CatId = @CatId
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_EntradaMaterialCreate
(
  @MaterialId     int
  , @Cantidad     float
  , @Comentarios  varchar(300)
  , @idUsuario    int
  , @FechaEntrada datetime
  , @Return_value int = -1 output
)
  as begin
    /*
    =================================================================================================================================
      #Id  Autor     Fecha        Description
    ---------------------------------------------------------------------------------------------------------------------------------
      01   DLuna     2020/04/14   Creación
    =================================================================================================================================
    */ 
    
    update 
      dbo.Tb_Material
    set
      Existencia = Existencia + @Cantidad
    where
      MaterialId = @MaterialId
    
    insert into dbo.Tb_EntradaMaterial
    (
      MaterialId     
      , Cantidad     
      , Comentarios  
      , idUsuario    
      , FechaEntrada
    )
    values 
    (
      @MaterialId     
      , @Cantidad     
      , @Comentarios  
      , @idUsuario    
      , @FechaEntrada
    )
    
    set @Return_value = SCOPE_IDENTITY()
    
    select @Return_value
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_SalidaMaterialCreate
(
  @MaterialId          int
  , @Cantidad          float
  , @Solicitante       varchar(100)
	, @Departamento      varchar(50)
  , @Comentarios       varchar(300)
  , @idInsumoFichaProd int
  , @idUsuario         int
  , @FechaSalida       datetime
)
  as begin
    /*
    =================================================================================================================================
      #Id  Autor     Fecha        Description
    ---------------------------------------------------------------------------------------------------------------------------------
      01   DLuna     2020/04/18   Creación
    =================================================================================================================================
    */
    
    set nocount on; 
    
    declare @Return_Value int
    set @Return_Value = -1
    
    -- Validar que la cantidad em inventario sea suficiente
    if ((select Existencia from dbo.Tb_Material where MaterialId = @MaterialId) < @Cantidad)
    begin
      set @Return_value = 0
    end
    
    if (@Return_value = -1)
    begin
    
      update 
        dbo.Tb_Material
      set
        Existencia = Existencia - @Cantidad
      where
        MaterialId = @MaterialId
      
      insert into dbo.Tb_SalidaMaterial
      (
        MaterialId     
        , Cantidad     
        , Solicitante
        , Departamento
        , Comentarios  
        , idInsumoFichaProd
        , idUsuario    
        , FechaSalida
      )
      values 
      (
        @MaterialId     
        , @Cantidad     
        , @Solicitante
        , @Departamento
        , @Comentarios  
        , @idInsumoFichaProd
        , @idUsuario    
        , @FechaSalida
      )
    
      set @Return_value = SCOPE_IDENTITY()
    end
    
    select [Return_value] = @Return_value
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_MaterialGetCollectionByCatDetTipoMaterialId 
(
  @CatDetTipoMaterialId  int
)
  as begin
    /*
    =================================================================================================================================
      #Id  Autor     Fecha        Description
    ---------------------------------------------------------------------------------------------------------------------------------
      01   DLuna     2020/04/20   Creación
    =================================================================================================================================
    */ 
    
    select
      MaterialId
      , Codigo
      , m.Descripcion
      , Existencia
      , m.idUnidadMedida
      , Precio
      , m.idTipoMoneda
      , CatDetTipoMaterialId
      , CatDetClasificacionId
      , CatDetEstatusId
      , m.FechaUltimaAct
      , [UnidadMedida] = um.descripcion
      , [TipoMoneda] = tm.descripcion
      , [TipoMaterial] = cdT.Nombre
      , [Clasificacion] = cdC.Nombre
      , [Estatus] = cdE.Nombre
    
    from
      dbo.Tb_Material m
    
      inner join
        tb_unidadMedida um
      on
        um.idUnidadMedida = m.idUnidadMedida
      
      inner join
        tb_tipoMoneda tm
      on
        tm.idTipoMoneda = m.idTipoMoneda
      
      inner join
        Tb_CatalogoDet cdT
      on
        cdT.CatDetId = m.CatDetTipoMaterialId
        
      inner join
        Tb_CatalogoDet cdC
      on
        cdC.CatDetId = m.CatDetClasificacionId
        
      inner join
        Tb_CatalogoDet cdE
      on
        cdE.CatDetId = m.CatDetEstatusId
    
    where
      CatDetTipoMaterialId = @CatDetTipoMaterialId
      
    order by
      m.Descripcion asc

  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure [dbo].[Usp_InsumosFichaProdUpdateEstatusSurtido]
(
  @IdFichaProd  int
)
  as begin
    /*
    =================================================================================================================================
      #Id  Autor     Fecha        Description
    ---------------------------------------------------------------------------------------------------------------------------------
      01   JOlmedo   2020/05/02   Creación
    =================================================================================================================================
    */
    set nocount on; 
      
      declare 
        @Return_Value int
        
      set 
        @Return_Value = -1
      
      -- Validar que la cantidad en inventario sea suficiente
      if (select idFichaProd from dbo.tb_InsumosFichaProd where idFichaProd = @IdFichaProd) = null
      begin
        set @Return_value = 0
      end
      
      if (@Return_value = -1)
      begin 
        update 
          dbo.tb_InsumosFichaProd  
        set 
          CatDetEstatusSurtidoId = 29 
        where 
          idFichaProd = @IdFichaProd;
        
        set @Return_value = (select count(idFichaProd) from dbo.tb_InsumosFichaProd where idFichaProd = @idFichaProd)
      end
      
      select [Return_value] = @Return_value
      
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.sp_agrFormSubProc 
(
  @idSubProceso             int
  , @CatDetOrigenMaterialId int -- #02
)
  as begin
    
    /*
    =================================================================================================================================
      #Id  Autor     Fecha        Description
    ---------------------------------------------------------------------------------------------------------------------------------
      01   ???       ???          Creación
      02   DLuna     2020/05/03   Agregar parametro @CatDetOrigenMaterialId para insertar en nuevo campo de la tabla tb_formXsubProc
    =================================================================================================================================
    */ 
    
    insert into dbo.tb_formXsubProc
    (
      idSubproceso
      , fechaCreacion
      , CatDetOrigenMaterialId
    )

    values 
    (
      @idSubproceso
      , getdate() -- #02
      , @CatDetOrigenMaterialId -- #02
    )
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.sp_agrInsumXProc 
(
  @idSubProceso     int
  , @clave          varchar(50)
  , @porcentaje     float
  , @nombreProducto varchar(60)
  , @comentario     varchar(100)
  , @idInsumo       int
  , @MaterialId     int -- #02
)
  as begin

  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   ???       ???          Creación
    02   DLuna     2020/05/03   Agregar parametro @MaterialId para insertar en nuevo campo de la tabla tb_insumXproc
  =================================================================================================================================
  */ 

    declare 
      @idFormXSubProc int
    
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
      (
        idFormXSubProc
        , clave
        , porcentaje
        , idInsumo
        , nombreProducto
        , comentario
        , MaterialId -- #02
      )
      
    values 
      (
        @idFormXSubProc
        , @clave
        , @porcentaje
        , @idInsumo
        , @nombreProducto
        , @comentario
        , @MaterialId -- #02
      )
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.sp_obtFormInsXSubProc 
(
  @idSubProceso int
)
  as begin
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   ???       ???          Creación
    02   DLuna     2020/05/04   Validar origenes de tabla de insumos (material)
  =================================================================================================================================
  */ 
  
    declare 
      @idFormXSubProc           int
      , @CatDetOrigenMaterialId int --#02
      , @CatDetBdPropia         int --#02
      , @CatDetBdContpaq        int --#02
      
    -- #02 {
    select
      @CatDetBdPropia = 27
      , @CatDetBdContpaq = 28
            
    select
      @idFormXSubProc = idFormXSubProc
      , @CatDetOrigenMaterialId = CatDetOrigenMaterialId
    from 
      dbo.tb_formXsubProc
    where 
      fechaCreacion = 
      (
        select 
          max (fechaCreacion)
          
        from 
          dbo.tb_formXsubProc
          
        where 
          idSubproceso = @idSubProceso
      )
      and idSubproceso = @idSubProceso
    
    select
      idInsumXProc
      , idFormXSubProc
      , clave
      , porcentaje
      , [idInsumo] = 
          case @CatDetOrigenMaterialId
            when @CatDetBdPropia then MaterialId
            else idInsumo
          end
      , nombreProducto
      , comentario
      , CatDetOrigenMaterialId = @CatDetOrigenMaterialId
      , MaterialId
      , [precioUnitario] = 
          isnull(
            case @CatDetOrigenMaterialId
              when @CatDetBdContpaq then
                (select dbo.Fn_ObtPrecioInsumoContpaq(ip.idInsumo))
              else 
                (
                  select
                    case m.idTipoMoneda
                      when 1 then m.Precio
                      else m.Precio * (select tipoCambio from dbo.tb_tipoMoneda where idTipoMoneda = m.idTipoMoneda) 
                    end
                  from
                    dbo.Tb_Material m
                  where
                    m.MaterialId = ip.MaterialId
                )
              end
          ,0)
      
    from 
      tb_insumXproc ip
      
    where 
      idFormXSubProc = @idFormXSubProc
      
    -- #02 }
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.sp_InsInsumosFichaProd
(
	@idFichaProd               int
  , @idProceso               int
  , @idSubproceso            int
  , @idFormXSubProc          int
  , @totalInsumos            float
  , @CatDetEstatusSurtidoId  int -- #02
)
  as begin
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   ???       ???          Creación
    02   DLuna     2020/05/04   agregar insert del campo CatDetEstatusSurtidoId
  =================================================================================================================================
  */ 
  
    insert into
      tb_InsumosFichaProd
      (
        idFichaProd
        , idProceso
        , idSubproceso
        , idFormXSubProc
        , totalInsumos
        , CatDetEstatusSurtidoId
      )
      
    values
      (
        @idFichaProd
        , @idProceso
        , @idSubproceso
        , @idFormXSubProc
        , @totalInsumos
        , @CatDetEstatusSurtidoId
      )
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.sp_InsInsumosFichaProdDet
(
	@idInsumoFichaProd int
  , @clave           varchar(50)
  , @porcentaje      float
  , @material        varchar(60)
  , @temperatura     varchar(50)
  , @rodar           varchar(50)
  , @cantidad        float
  , @observaciones   varchar(100)
  , @precioUnitario  float
  , @total           float
  , @MaterialId      int -- #02
)
  as begin
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   ???       ???          Creación
    02   DLuna     2020/05/04   agregar insert del campo MaterialId
  =================================================================================================================================
  */ 

    insert into
      tb_InsumosFichaProdDet
      (
        idInsumoFichaProd
        , clave
        , porcentaje
        , material
        , temperatura
        , rodar
        , cantidad
        , observaciones
        , precioUnitario
        , total
        , MaterialId --#02
      )
      
    values
      (
        @idInsumoFichaProd
        , @clave
        , @porcentaje
        , @material
        , @temperatura
        , @rodar
        , @cantidad
        , @observaciones
        , @precioUnitario
        , @total
        , @MaterialId
      )
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_MaterialUpdate
(
	@MaterialId              int
  , @Codigo                varchar(10)
  , @idUnidadMedida        int
  , @Precio                float
  , @idTipoMoneda          int
	, @CatDetTipoMaterialId  int
	, @CatDetClasificacionId int
	, @CatDetEstatusId       int
  , @Respuesta             int = -1 output
  , @Mensaje               varchar(60) = '' output

)
  as begin
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   DLuna     2020/05/05   Creación
  =================================================================================================================================
  */
    
    if exists (select 1 from dbo.Tb_Material where Codigo = @Codigo and MaterialId != @MaterialId)
    begin
    
      select 
        @Respuesta = 0
        , @Mensaje = 'El código capturado ya ha sido registrado con otro material'
    end
    
    else
    begin

      update 
        dbo.Tb_Material
        
      set
        Codigo = @Codigo
        , idUnidadMedida = @idUnidadMedida
        , Precio = @Precio
        , idTipoMoneda = @idTipoMoneda
        , CatDetTipoMaterialId = @CatDetTipoMaterialId
        , CatDetClasificacionId = @CatDetClasificacionId
        , CatDetEstatusId = @CatDetEstatusId
        , FechaUltimaAct = getdate()
        
      where
        MaterialId = @MaterialId
        
      select 
        @Respuesta = @@ROWCOUNT
        , @Mensaje = 'Material editado correctamente'
    end
    
    select 
      @Respuesta
      , @Mensaje
      
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_InsumosFichaProdValidarSurtido
(
	@idFichaProd int
  , @Respuesta int = -1 output
  , @Mensaje   varchar(80) = '' output

)
  as begin
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   DLuna     2020/05/06   Creación
  =================================================================================================================================
  */
  
  declare
    @idInsumoFichaProd        int
    , @idFormXSubProc         int
    , @CatDetEstatusSurtidoId int
    , @CatDetNoSurtida        int
    , @CatDetBdPropia         int
  
  select
    @idInsumoFichaProd = 0
    , @idFormXSubProc  = 0
    , @CatDetBdPropia  = 27
    , @CatDetNoSurtida   = 30
  
  select
    @idInsumoFichaProd = idInsumoFichaProd
    , @idFormXSubProc = idFormXSubProc
    , @CatDetEstatusSurtidoId = CatDetEstatusSurtidoId
  from
    dbo.tb_InsumosFichaProd
  where
    idFichaProd = @idFichaProd
  
  if (@idInsumoFichaProd = 0 or @idInsumoFichaProd is null)
  begin
    select 
      @Respuesta = 0
      , @Mensaje = 'No existe la ficha capturada'
  end
  
  else begin
  
    if exists (select 1 from dbo.tb_formXsubProc where idFormXSubProc = @idFormXSubProc and CatDetOrigenMaterialId = @CatDetBdPropia)
    begin
      
      if (@CatDetEstatusSurtidoId = @CatDetNoSurtida)
      begin
        select
          @Respuesta = @idInsumoFichaProd
          , @Mensaje = 'Lista para surtir'
      end
      
      else begin
        select 
          @Respuesta = 0
          , @Mensaje = 'La ficha de producción ya ha sido surtida anteriormente'
      end
        
    end
    
    else begin
      select 
        @Respuesta = 0
        , @Mensaje = 'No se puede surtir una ficha con insumos de un origen de datos distinto'
    end
    
  end
    
  select 
    @Respuesta
    , @Mensaje
      
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_MaterialGetCollectionByIdInsumoFichaProd
(
	@idInsumoFichaProd int
)
  as begin
  /*
  =================================================================================================================================
    #Id  Autor     Fecha        Description
  ---------------------------------------------------------------------------------------------------------------------------------
    01   DLuna     2020/05/06   Creación
  =================================================================================================================================
  */
  
  select
    ifpd.idInsumoFichaProd
    , m.MaterialId
    , m.Codigo
    , [Material] = m.descripcion
    , [UnidadMedida] = um.descripcion
    , ifpd.CantidadSolicitada
    , m.Existencia
    , CantidadSuficiente = 
        case
          when ifpd.CantidadSolicitada <= m.Existencia
           then 1
           else 0
        end
  
  from
    dbo.Tb_Material m
  
    inner join
      dbo.Vw_InsumosFichaProdDet ifpd
    on
      ifpd.MaterialId = m.MaterialId
      and ifpd.idInsumoFichaProd = @idInsumoFichaProd
      
    inner join
      dbo.tb_UnidadMedida um
    on
      um.idUnidadMedida = m.idUnidadMedida
      
  end
go