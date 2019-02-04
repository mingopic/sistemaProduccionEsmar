use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_eliFichaProdDet')
begin 
  drop
    procedure sp_eliFichaProdDet
end
go

create procedure sp_eliFichaProdDet
(
  @idFichaProd        int
  , @noPiezasDevolver int
  , @idPartidaDet     int
)
  as begin
  
    delete from
      tb_fichaProdDet
    where
      idFichaProd = @idFichaProd
    
    update
      tb_partidaDet
    set
      noPiezasAct = noPiezasAct + @noPiezasDevolver
    where
      idPartidaDet = @idPartidaDet
  end
go