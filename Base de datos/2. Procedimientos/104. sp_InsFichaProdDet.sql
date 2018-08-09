use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_InsFichaProdDet')
begin 
  drop
    procedure sp_InsFichaProdDet
end
go

create procedure sp_InsFichaProdDet
  (
    @idFichaProd      int not null foreign key references tb_proceso(idProceso)
    , @idPartidaDet   int not null foreign key references tb_proceso(idProceso)
    , @noPiezasTotal  int
    , @kgTotal        float
    , @costoInsumos   float
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