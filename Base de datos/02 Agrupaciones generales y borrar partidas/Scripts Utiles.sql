/*
select 
  distinct(fp.idFichaProd)
  
from 
  dbo.tb_fichaProd fp
  
  left join 
    tb_fichaProdDet fpd
  on 
    fpd.idFichaProd = fp.idFichaProd
  
  inner join
    tb_partidaDet pd
  on
    pd.idPartidaDet = fpd.idPartidaDet
  
  inner join
    tb_partida p
  on
    p.idPartida = pd.idPartida
    and p.idPartida = 37
*/    

select
  p.noPartida, fpd.idFichaProd, pd.idProceso

from
  tb_partida p
  
  inner join
    tb_partidaDet pd
  on
    pd.idPartida = p.idPartida
  
  inner join
    tb_fichaProdDet fpd
  on
    fpd.idPartidaDet = pd.idPartidaDet
    and fpd.idFichaProd in 
    (
      select 
        distinct(fp.idFichaProd)
        
      from 
        dbo.tb_fichaProd fp
        
        left join 
          tb_fichaProdDet fpd
        on 
          fpd.idFichaProd = fp.idFichaProd
        
        inner join
          tb_partidaDet pd
        on
          pd.idPartidaDet = fpd.idPartidaDet
        
        inner join
          tb_partida p
        on
          p.idPartida = pd.idPartida
          and p.idPartida = 37
        
        inner join
          tb_proceso pro
        on
          pro.idProceso = pd.idProceso
    )
