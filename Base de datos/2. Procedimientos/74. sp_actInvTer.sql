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
  , @kg             float
  , @decimetros     float
  , @pies           float
)
as begin

  declare @promKg         float
  declare @promDecimetros float
  declare @promPies       float
  declare @kgDesc         float
  declare @decimetrosDesc float
  declare @piesDesc       float
  
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
  
  set @kgDesc = @promKg*@piezasUtilizar
  
  if (@kgDesc > @kg)
  begin
    set @kgDesc = @kg
  end
  
  set @decimetrosDesc = @promDecimetros*@piezasUtilizar
  
  if (@decimetrosDesc > @decimetros)
  begin
    set @decimetrosDesc = @decimetros
  end
  
  set @piesDesc = @promPies*@piezasUtilizar
  
  if (@piesDesc > @pies)
  begin
    set @piesDesc = @pies
  end

  update
    tb_invTerminado
    
  set
    noPiezasActuales = noPiezasActuales-@piezasUtilizar
    , kgTotalesActual = kgTotalesActual-(@kgDesc)
    , decimetrosActual = decimetrosActual-(@decimetrosDesc)
    , piesActual = piesActual-(@piesDesc)
    
  where
    idInvTerminado = @idInvTerminado
end
go