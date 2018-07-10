use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_rangoPesoCuero')
begin 
  drop
    table tb_rangoPesoCuero 
end
go

create table tb_rangoPesoCuero 
(
  idRangoPesoCuero int not null identity(1,1) primary key
  , rangoMin       float
  , rangoMax       float
  , fechaConfig    datetime
);
go