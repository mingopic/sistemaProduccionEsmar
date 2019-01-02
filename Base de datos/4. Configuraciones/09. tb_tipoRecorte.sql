use esmarProd
go

insert into
  tb_tipoRecorte
values
  ('Entero'), ('Delantero Sillero'), ('Crupon Sillero'), ('Lados'), ('Centro Castaño'), ('Centro Quebracho'), ('Delantero Suela'), 
  ('Centro'), ('Delantero Doble'), ('Delantero Sillero Toro'), ('Delantero Suela Doble'), ('Sillero Teñido Café'), ('Sillero Teñido Negro'), ('Sillero Teñido Tang');

update 
  tb_tipoRecorte 
set 
  descripcion = replace(descripcion, 'Ã±', 'ñ')