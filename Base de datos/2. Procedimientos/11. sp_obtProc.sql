use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtProc')
begin 
  drop
    procedure sp_obtProc
end
go

create procedure sp_obtProc
  as begin
  
    declare 
      @procInicio int
      , @procFin  int
    
    set @procInicio = 1
    set @procFin    = 7

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