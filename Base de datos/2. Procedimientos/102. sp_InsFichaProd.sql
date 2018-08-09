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
    , @kgTotal        float
    , @fechaCreacion  date
  )

as begin
  
  insert into
    tb_fichaProd
    (
      idTambor
      , noPiezasTotal
      , kgTotal
      , fechaCreacion
    )
  
  values
   (
     @idTambor
      , @noPiezasTotal
      , @kgTotal
      , @fechaCreacion 
   )
end
go