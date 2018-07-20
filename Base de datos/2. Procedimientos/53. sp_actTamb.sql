use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actTamb')
begin 
  drop
    procedure sp_actTamb
end
go

create procedure sp_actTamb
  (
    @nombreTambor varchar(100)
    , @estatus       int
    , @idTambor   int
  )
  as begin
  
    update
      tb_tambor 
      
    set
      nombreTambor = @nombreTambor
      , estatus = @estatus
      
    where
      idTambor = @idTambor
  end
go