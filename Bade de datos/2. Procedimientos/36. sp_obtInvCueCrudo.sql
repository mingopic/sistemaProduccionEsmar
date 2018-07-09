use esmarProd
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
    , rc.noTotalPiezas
    , rc.kgTotal
    , (rc.kgTotal/rc.noTotalPiezas) as pesoProm
    , rc.precioXKilo
    , rc.costoCamion
    
	from
		tb_tipoCuero as tc
    
    inner join
      tb_recepcionCuero as rc
    on
      tc.idTipoCuero = rc.idTipoCuero
      
    inner join
      tb_proveedor as p
    on
      rc.idProveedor = p.idProveedor
end
go