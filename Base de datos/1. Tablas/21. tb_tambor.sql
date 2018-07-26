use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_tambor')
begin 
  drop
    table tb_tambor 
end
go

create table tb_tambor 
(
  idTambor       int not null identity(1,1) primary key
  , nombreTambor varchar(100)
  , estatus         int
  
  , constraint un_nombreTambor unique(nombreTambor) 
);
go