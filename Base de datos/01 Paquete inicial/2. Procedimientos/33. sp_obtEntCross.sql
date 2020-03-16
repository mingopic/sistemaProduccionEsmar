use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtEntCross')
begin 
  drop
    procedure sp_obtEntCross
end
go

create procedure sp_obtEntCross
(
	@tipoRecorte varchar(20)
	, @noPartida int
	, @fecha varchar(10)
	, @fecha1 varchar(10)
  , @accion int = 0
)
as begin

  if @accion = 0
  begin
  
    select 
      p.noPartida
      , tr.descripcion
      , ic.noPiezas
      , ic.noPiezasActuales
      , ic.kgTotal
      , ic.kgActual
      , ic.fechaentrada
      , ic.idInvPCross
      
    from 
      tb_invCross as ic
      
      inner join
        tb_partidaDet as pd
      on
        pd.idPartidaDet = ic.idPartidaDet
      
      inner join
        tb_partida as p
      on
        p.idPartida = pd.idPartida
        
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = pd.idTipoRecorte
        and tr.descripcion like @tipoRecorte
        
    where
      ic.fechaentrada between @fecha and @fecha1
      and ic.noPiezasActuales > 0
      and 
      (
        (
          @noPartida = 0
          and p.noPartida > 0
        )
        or
        (
          @noPartida > 0
          and p.noPartida = @noPartida
        )
      )
  end
  
  else begin
    select 
      p.noPartida
      , tr.descripcion
      , ic.noPiezas
      , ic.noPiezasActuales
      , ic.kgTotal
      , ic.kgActual
      , ic.fechaentrada
      , ic.idInvPCross
      
    from 
      tb_invCross as ic
      
      inner join
        tb_partidaDet as pd
      on
        pd.idPartidaDet = ic.idPartidaDet
      
      inner join
        tb_partida as p
      on
        p.idPartida = pd.idPartida
        
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = pd.idTipoRecorte
        and tr.descripcion like @tipoRecorte
        
    where
      ic.fechaentrada between @fecha and @fecha1
      and 
      (
        (
          @noPartida = 0
          and p.noPartida > 0
        )
        or
        (
          @noPartida > 0
          and p.noPartida = @noPartida
        )
      )
  end
end
go