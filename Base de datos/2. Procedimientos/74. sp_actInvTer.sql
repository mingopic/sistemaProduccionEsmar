use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actInvTer')
begin 
  drop
    procedure sp_actInvTer
end
go

create procedure sp_actInvTer
(
  @idInvTerminado   int
  , @piezasUtilizar int
)
as begin

  declare @promKg         float
  declare @promDecimetros float
  declare @promPies       float
  
  set @promKg =
  (
    select
      (kgTotales/noPiezas)
    from
      tb_invTerminado
    where
      idInvTerminado = @idInvTerminado
  )
  
  set @promDecimetros =
  (
    select
      (decimetros/noPiezas)
    from
      tb_invTerminado
    where
      idInvTerminado = @idInvTerminado
  )
  
  set @promPies =
  (
    select
      (pies/noPiezas)
    from
      tb_invTerminado
    where
      idInvTerminado = @idInvTerminado
  )

  update
    tb_invTerminado
    
  set
    noPiezasActuales = noPiezasActuales-@piezasUtilizar
    , kgTotalesActual = kgTotalesActual-(@promKg*@piezasUtilizar)
    , decimetrosActual = decimetrosActual-(@promDecimetros*@piezasUtilizar)
    , piesActual = piesActual-(@promPies*@piezasUtilizar)
    
  where
    idInvTerminado = @idInvTerminado
end
go