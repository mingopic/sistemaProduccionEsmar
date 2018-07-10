use esmarProd
go

if exists (select name from sys.sysobjects WHERE name = 'sp_obtInvCueCrudo')
begin 
  drop
    procedure sp_obtInvCueCrudo
end
go

create procedure sp_obtInvCueCrudo
as begin

	declare @descripcion varchar(30)
	declare @tipoRecorte varchar(30)
	
	set @tipoRecorte =
	(
		select
			descripcion
      
		from
			tb_tipoRecorte
      
		where
			idTipoRecorte = 1
	)

	select
		concat(@tipoRecorte,' ',tc.descripcion) as descripcion
    , p.nombreProveedor
    , ic.noPiezasActual
    , rc.kgTotal
    , (rc.kgTotal/ic.noPiezasActual) as pesoProm
    , rc.precioXKilo
    , rc.costoCamion
    
	from
		tb_inventarioCrudo as ic
	inner join
		tb_recepcionCuero as rc
	on
		rc.idRecepcionCuero = ic.idRecepcionCuero
	
	inner join
		tb_tipoCuero as tc
    on
      tc.idTipoCuero = rc.idTipoCuero
      
    inner join
      tb_proveedor as p
    on
      p.idProveedor = rc.idProveedor
end
go