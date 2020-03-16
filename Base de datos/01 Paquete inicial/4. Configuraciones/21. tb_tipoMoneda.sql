use esmarProd
go

insert into 
  tb_tipoMoneda 
    (
      descripcion
      , abreviacion
      , tipoCambio
      , fechaMod
    ) 
values 
  (
    'Peso MXN'
    , 'MXN'
    , 1.00
    , getdate()
  )
  ,
  (
    'Dolar'
    , 'Dolar'
    , 1.00
    , '1990-01-01'
  )