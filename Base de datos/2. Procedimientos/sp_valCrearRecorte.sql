use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_valCrearRecorte')
begin 
  drop
    procedure sp_valCrearRecorte
end
go

create procedure sp_valCrearRecorte
(
	@idPartida             int
  , @idRecepcionCuero    int
  , @idTipoRecorte       int
  , @idProceso           int
)
as begin
  
  if (@idProceso = 2)
  begin
    
    if (@idTipoRecorte = 0)
    begin
      
      set @idTipoRecorte = 2
    end
    
    else if (@idTipoRecorte = 1)
    begin
      
      set @idTipoRecorte = 5
    end
    
    else if (@idTipoRecorte = 2)
    begin
      
      set @idTipoRecorte = 6
    end
    
    
    if exists
    (
      select
        1
      from
        tb_partidaDet
      where
        idPartida = @idPartida
        and idRecepcionCuero = @idRecepcionCuero
        and idTipoRecorte = @idTipoRecorte
        and idProceso = 1
    )
    begin
      
      select
        [bandera] = 0
    end
    
    else begin
    
      select
        [bandera] = 1
    end
  
  end
  
  else begin
    
      select
        [bandera] = 1
  end
  
end
go