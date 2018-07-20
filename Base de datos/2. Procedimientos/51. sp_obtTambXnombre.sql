use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtTambXnombre')
begin 
  drop
    procedure sp_obtTambXnombre
end
go

create procedure sp_obtTambXnombre
  (
    @nombreTambor varchar(100)
  )
  as begin
    select 
      *
      
    from
      tb_tambor
      
    where
      nombreTambor = @nombreTambor
  end
go