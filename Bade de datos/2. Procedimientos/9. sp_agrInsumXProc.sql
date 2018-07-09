use esmarProd
go
 
create procedure sp_agrInsumXProc 
  (
    @idSubProceso int
    , @clave      varchar(10)
    , @porcentaje float
    , @idInsumo   int
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