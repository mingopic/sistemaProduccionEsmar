  use esmarProd
  go

  if exists (select name from sys.sysobjects WHERE name = 'sp_obtEntInvSemTer')
  begin 
    drop
      procedure sp_obtEntInvSemTer
  end
  go

  create procedure sp_obtEntInvSemTer
  (
    @tipoRecorte varchar(20)
  )
  as begin
    select
      tr.descripcion as tipoRecorte
      , sum(ist.noPiezasActuales) as noPiezas
      
    from
      tb_invSemTer as ist
      
      inner join
        tb_invSemiterminado as ins
      on
        ins.idInvSemiterminado = ist.idInvSemiterminado
        
      inner join
        tb_invCrossSemi as ics
      on
        ics.idInvCrossSemi = ins.idInvCrossSemi
        
      inner join
        tb_invCross as ic
      on
        ic.idInvPCross = ics.idInvPCross
      inner join
        tb_partidaDet as pd
      on
        pd.idPartidaDet = ic.idPartidaDet
      inner join
        tb_tipoRecorte as tr
      on
        tr.idTipoRecorte = pd.idTipoRecorte
        and tr.descripcion like @tipoRecorte
        
    where
      ist.noPiezasActuales > 0
      
    group by
      tr.descripcion
      
    ------------------------------------------------------------------------
    union all
    ------------------------------------------------------------------------
    
    select
      tr.descripcion as tipoRecorte
      , sum (istp.noPiezasActuales) as noPiezas
      
    from
      tb_invSemTerPesado istp
      
      inner join
        tb_CueroPesado cp
      on
        cp.idInventario = istp.idInventario
        
      inner join
        tb_tipoRecorte tr
      on
        tr.idTipoRecorte = cp.idTipoRecorte
        and tr.descripcion like @tipoRecorte
        
    where
      istp.noPiezasActuales > 0
      
    group by
      tr.descripcion
      
    ------------------------------------------------------------------------
    union all
    ------------------------------------------------------------------------
    
    select
      tr.descripcion as tipoRecorte
      , sum (istm.noPiezasActuales) as noPiezas
      
    from
      tb_invSemTerManual istm
        
      inner join
        tb_tipoRecorte tr
      on
        tr.idTipoRecorte = istm.idTipoRecorte
        and tr.descripcion like @tipoRecorte
        
    where
      istm.noPiezasActuales > 0
      
    group by
      tr.descripcion
  end