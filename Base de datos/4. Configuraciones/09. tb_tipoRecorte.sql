use esmarProd
go

insert into
  tb_tipoRecorte
values
  ('Entero') ,('Delantero Sillero'), ('Crupon Sillero'), ('Lados'), ('Centro Castaño'), ('Centro Quebracho'), ('Delantero Suela');

update 
  tb_tipoRecorte 
set 
  descripcion = replace(descripcion, 'Ã±', 'ñ')