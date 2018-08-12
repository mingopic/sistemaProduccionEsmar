use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_InsInsumosFichaProd')
begin 
  drop
    procedure sp_InsInsumosFichaProd
end
go

create procedure sp_InsInsumosFichaProd
(
	@idFichaProd      int
  , @idProceso      int
  , @idSubproceso   int
  , @idFormXSubProc int
  , @totalInsumos   float
)
as begin

  insert into
    tb_InsumosFichaProd
    (
      idFichaProd
      , idProceso
      , idSubproceso
      , idFormXSubProc
      , totalInsumos
    )
    
  values
    (
      @idFichaProd
      , @idProceso
      , @idSubproceso
      , @idFormXSubProc
      , @totalInsumos
    )
end
go