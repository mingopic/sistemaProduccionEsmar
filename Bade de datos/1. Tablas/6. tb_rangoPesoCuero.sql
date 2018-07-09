use esmarProd
go

create table tb_rangoPesoCuero 
(
  idRangoPesoCuero int not null identity(1,1) primary key
  , rangoMin       float
  , rangoMax       float
  , fechaConfig    datetime
);
go