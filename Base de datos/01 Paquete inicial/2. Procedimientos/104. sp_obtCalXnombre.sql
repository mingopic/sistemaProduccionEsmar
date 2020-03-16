use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtCalXnombre')
begin 
  drop
    procedure sp_obtCalXnombre
end
go

create procedure sp_obtCalXnombre
  (
    @calibre varchar(100)
  )
  as begin
    select 
      *
      
    from
      tb_calibre
      
    where
      descripcion = @calibre
  end
go