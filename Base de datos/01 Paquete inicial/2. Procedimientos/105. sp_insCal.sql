use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insCal')
begin 
  drop
    procedure sp_insCal
end
go

create procedure sp_insCal
  (
    @calibre   varchar(100)
    , @estatus int
  )
  as begin
  
    insert into
      tb_calibre 
      
    values
      (
        @calibre
        , @estatus
      )
  end
go