use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_actInvSemiterminado')
begin 
  drop
    procedure sp_actInvSemiterminado
end
go

create procedure sp_actInvSemiterminado
(
  @idInvSemiterminado	int
  , @piezasUtilizar 	int
  , @kg               float
)
as begin

  declare @promKg float
  declare @kgDesc float
  
  set @promKg =
  (
    select
      (kgTotales/noPiezas)
    from
      tb_invSemiterminado
    where
      idInvSemiterminado = @idInvSemiterminado
  )
  
  set @kgDesc = @promKg*@piezasUtilizar
  
  if (@kgDesc > @kg)
  begin
    set @kgDesc = @kg
  end

  update
    tb_invSemiterminado
    
  set
    noPiezasActuales = noPiezasActuales-@piezasUtilizar
    , kgTotalesActuales = kgTotalesActuales-(@kgDesc)
    
  where
    idInvSemiterminado = @idInvSemiterminado
end
go