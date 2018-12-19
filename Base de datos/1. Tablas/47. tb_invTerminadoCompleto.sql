use esmarProd
go

if exists (select name from sys.tables WHERE name = 'tb_invTerminadoCompleto')
begin 
  drop
    table tb_invTerminadoCompleto 
end
go

create table tb_invTerminadoCompleto
(
  idInvTerminado        int
  , bandera             int -- 0 tb_invTerminado y 1 tb_invTerminadoPesado
  , noPartida           int
  , idTipoRecorte       int
  , idCalibre           int
  , idSeleccion         int
  , noPiezasActuales    int
  , kgTotalesActual     float
  , decimetrosActual    float
  , piesActual          float
  , fechaEntrada	      date
);
go
