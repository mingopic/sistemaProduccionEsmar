use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtTambAct')
begin 
  drop
    procedure sp_obtTambAct
end
go

create procedure sp_obtTambAct
  as begin
  
    select 
      idTambor
      , nombreTambor 
      
    from 
      tb_tambor 
      
    where 
      estatus = 1
  end
go