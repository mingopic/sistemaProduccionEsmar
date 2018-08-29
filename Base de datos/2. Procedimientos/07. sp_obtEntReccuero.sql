use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtEntReccuero')
begin 
  drop
    procedure sp_obtEntReccuero
end
go

create procedure sp_obtEntReccuero 
  (
    @proveedor   varchar(100)
    , @tipoCuero varchar(20)
    , @fecha     varchar(10)
    , @fecha1    varchar(10)
  )
  as begin
  
    select 
      rc.idrecepcioncuero
      , p.nombreproveedor
      , tp.descripcion
      , rc.nocamion
      , rc.nototalpiezas
      , rc.kgtotal
      , rc.precioxkilo 
      ,(rc.kgtotal*rc.precioxkilo) as costocamion
      , rc.origen
      , rc.fechaentrada
      
    from 
      tb_proveedor as p 
      
      inner join 
        tb_recepcioncuero as rc
      on 
        p.idproveedor = rc.idproveedor
        
      inner join 
        tb_tipocuero as tp
      on 
        rc.idtipocuero = tp.idtipocuero
        
    where 
      p.nombreproveedor like @proveedor
      and tp.descripcion like @tipoCuero
      and rc.fechaentrada between @fecha and @fecha1
  end
go