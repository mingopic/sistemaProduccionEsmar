use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtUnidadMedida')
begin 
  drop
    procedure sp_obtUnidadMedida
end
go

create procedure sp_obtUnidadMedida
as begin
  
  select
    idUnidadMedida
    , descripcion
    , desCorta
  
  from
    tb_unidadMedida
end