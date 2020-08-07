
use esmarProd
go


if object_id('dbo.Usp_EntradaMaterialCreate') is not null
begin
	drop procedure dbo.Usp_EntradaMaterialCreate
end
go

if object_id('dbo.Usp_EntradasGetAll') is not null
begin
	drop procedure dbo.Usp_EntradasGetAll
end
go

if object_id('dbo.Usp_EntradaMaterialUpdate') is not null
begin
	drop procedure dbo.Usp_EntradaMaterialUpdate
end
go

if object_id('dbo.Usp_EntradaMaterialDelete') is not null
begin
	drop procedure dbo.Usp_EntradaMaterialDelete
end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_EntradaMaterialCreate
(
  @MaterialId     int
  , @Cantidad     float
  , @Precio       float -- #02
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
      02   DLuna     2020/08/06   Se agrega el campo y parámetro Precio
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
      , Precio --#02
    )
    values 
    (
      @MaterialId     
      , @Cantidad     
      , @Comentarios  
      , @idUsuario    
      , @FechaEntrada
      , @Precio --#02
    )
    
    set @Return_value = SCOPE_IDENTITY()
    
    select @Return_value
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_EntradasGetAll 
(
  @Codigo                   varchar(10) = ''
  , @CatDetTipoMaterialId   int = 0
  , @CatDetClasificacionId  int = 0
  , @fechainicio datetime = null
  , @fechafin datetime = null
)
  as begin
    /*
    =================================================================================================================================
      #Id  Autor     Fecha        Description
    ---------------------------------------------------------------------------------------------------------------------------------
      01   JOlmedo     2020/05/13   Creación
      02   DLuna       2020/08/06   Agregar campos EntradaMaterialId, MaterialId y Precio a la consulta
    =================================================================================================================================
    */
    select
      EntradaMaterialId -- #02
      , Codigo 
      , m.Descripcion
      , m.MaterialId -- #02
      , em.cantidad
      , [UnidadMedida] = um.descripcion
      , em.Precio -- #02
      , [TipoMaterial] = cdT.Nombre
      , [Clasificacion] = cdC.Nombre
      , [Comentarios] = em.Comentarios
      , [FechaEntrada] = CONVERT (date, em.FechaEntrada)
    
    from
      dbo.Tb_EntradaMaterial em

      inner join
		Tb_Material m
	  on 
		em.MaterialId = m.MaterialId
    
      inner join
        tb_unidadMedida um
      on
        um.idUnidadMedida = m.idUnidadMedida
      
      inner join
        Tb_CatalogoDet cdT
      on
        cdT.CatDetId = m.CatDetTipoMaterialId
        
      inner join
        Tb_CatalogoDet cdC
      on
        cdC.CatDetId = m.CatDetClasificacionId

        
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
      and em.FechaEntrada between 
           (select 
              case 
              when @fechainicio is null
              then  (SELECT DATEADD(year,-100, (select convert (date, getdate()))))
              else @fechainicio 
            end)
          and 
           (select 
              case 
              when @fechafin is null 
              then (SELECT DATEADD(year,100, (select convert (date, getdate()))))
              else @fechafin 
            end)

    order by
     em.FechaEntrada desc , m.Descripcion asc;
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_EntradaMaterialUpdate
(
  @EntradaMaterialId  int
  , @Cantidad         float
  , @Precio           float
  , @Comentarios      varchar(300)
  , @idUsuario        int
  , @FechaEntrada     datetime
  , @Return_value     int = -1 output
)
  as begin
    /*
    =================================================================================================================================
      #Id  Autor     Fecha        Description
    ---------------------------------------------------------------------------------------------------------------------------------
      01   DLuna     2020/08/06   Creación
    =================================================================================================================================
    */ 
    
    declare
      @CantidadPrevia float
      , @MaterialId   int
    
    select
      @CantidadPrevia = Cantidad
      , @MaterialId = MaterialId
    from
      dbo.Tb_EntradaMaterial
    where
      EntradaMaterialId = @EntradaMaterialId
    
    update 
      dbo.Tb_Material
    set
      Existencia = Existencia + (@Cantidad - @CantidadPrevia)
    where
      MaterialId = @MaterialId
    
    update 
      dbo.Tb_EntradaMaterial
    set  
      Cantidad = @Cantidad     
      , Comentarios = @Comentarios  
      , idUsuario = @idUsuario    
      , FechaEntrada = @FechaEntrada
      , Precio = @Precio
    where
      EntradaMaterialId = @EntradaMaterialId
    
    set @Return_value = @@ROWCOUNT
    
    select @Return_value
  end
go

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create procedure dbo.Usp_EntradaMaterialDelete
(
  @EntradaMaterialId  int
  , @Return_value     int = -1 output
)
  as begin
    /*
    =================================================================================================================================
      #Id  Autor     Fecha        Description
    ---------------------------------------------------------------------------------------------------------------------------------
      01   DLuna     2020/08/06   Creación
    =================================================================================================================================
    */ 
    
    declare
      @CantidadPrevia float
      , @MaterialId   int
    
    select
      @CantidadPrevia = Cantidad
      , @MaterialId = MaterialId
    from
      dbo.Tb_EntradaMaterial
    where
      EntradaMaterialId = @EntradaMaterialId
    
    update 
      dbo.Tb_Material
    set
      Existencia = Existencia - @CantidadPrevia
    where
      MaterialId = @MaterialId
    
    delete from 
      dbo.Tb_EntradaMaterial
    where
      EntradaMaterialId = @EntradaMaterialId
    
    set @Return_value = @@ROWCOUNT
    
    select @Return_value
  end
go

