use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actCal')
begin 
  drop
    procedure sp_actCal
end
go

create procedure sp_actCal
  (
    @calibre      varchar(100)
    , @estatus    int
    , @idCalibre  int
  )
  as begin
  
    update
      tb_calibre 
      
    set
      descripcion = @calibre
      , estatus = @estatus
      
    where
      idCalibre = @idCalibre
  end
go