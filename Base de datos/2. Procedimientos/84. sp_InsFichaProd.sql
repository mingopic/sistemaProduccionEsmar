use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_InsFichaProd')
begin 
  drop
    procedure sp_InsFichaProd
end
go

create procedure sp_InsFichaProd
  (
    @idTambor        int
    , @noPiezasTotal int
    , @kgTotal       float
    , @costoInsumos  float
    , @idSubproceso  int
  )

as begin
  
  insert into
    tb_fichaProd
    (
      idTambor
      , noPiezasTotal
      , kgTotal
      , costoInsumos
      , fechaCreacion
      , idSubproceso
    )
  
  values
   (
     @idTambor
      , @noPiezasTotal
      , @kgTotal
      , @costoInsumos
      , getdate()
      , @idSubproceso
   )
end
go