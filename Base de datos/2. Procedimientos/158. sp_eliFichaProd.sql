use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_eliFichaProd')
begin 
  drop
    procedure sp_eliFichaProd
end
go

create procedure sp_eliFichaProd
(
  @idFichaProd        int
)
  as begin
  
    delete from
      tb_fichaProd
    where
      idFichaProd = @idFichaProd
  end
go