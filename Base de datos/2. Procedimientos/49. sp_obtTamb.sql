use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtTamb')
begin 
  drop
    procedure sp_obtTamb
end
go

create procedure sp_obtTamb
  as begin
  
    select 
      nombreTambor
      , estatus
      
    from
      tb_tambor
  end
go