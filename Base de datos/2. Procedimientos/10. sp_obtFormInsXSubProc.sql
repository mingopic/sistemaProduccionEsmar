use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtFormInsXSubProc')
begin 
  drop
    procedure sp_obtFormInsXSubProc
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
          and idSubproceso = @idSubProceso
      )
    
    
    select
      idInsumXProc
      , idFormXSubProc
      , clave
      , porcentaje
      , idInsumo
      , nombreProducto
      , comentario
      
    from 
      tb_insumXproc
      
    where 
      idFormXSubProc = @idFormXSubProc
  end
go