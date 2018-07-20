use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_insTamb')
begin 
  drop
    procedure sp_insTamb
end
go

create procedure sp_insTamb
  (
    @nombreTambor varchar(100)
    , @estatus       int
  )
  as begin
  
    insert into
      tb_tambor 
      
    values
      (
        @nombreTambor
        , @estatus
      )
  end
go