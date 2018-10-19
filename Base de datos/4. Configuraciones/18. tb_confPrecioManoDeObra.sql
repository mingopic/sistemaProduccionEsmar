use esmarProd
go

insert into 
  tb_confPrecioManoDeObra 
    (
      idTipoRecorte
      , costo
      , fecha
    ) 
values 
  (
    1
    , 397
    , getdate()
  )
  , 
  (
    2
    , 119.10
    , getdate()
  )
  , 
  (
    3
    , 277.90
    , getdate()
  )
  , 
  (
    4
    , 198.5
    , getdate()
  )
  , 
  (
    5
    , 138.95
    , getdate()
  )
  ,
  (
    6
    , 138.95
    , getdate()
  )
  ,
  (
    7
    , 59.55
    , getdate()
  )
  ,
  (
    8
    , 138.95
    , getdate()
  );