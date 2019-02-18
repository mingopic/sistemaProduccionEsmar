/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Insumo;
import Modelo.InsumoPorProceso;
import Modelo.SubProceso;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Equipo
 */
public class InsumoPorProcesoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    static InsumoPorProceso ipp;
    static SubProceso subP;
    
    //Método para agregar una entrada a la tabla entradaProductoAlmacen
    public static void agregarInsumoXProc(InsumoPorProceso[] datosIXP, int idSubProceso) throws Exception {
        for (int i = 0; i < datosIXP.length; i++) {
            String query = "exec sp_agrInsumXProc "
                    + idSubProceso 
                    + ", '" + datosIXP[i].getClave() + "'"
                    + "," + datosIXP[i].getPorcentaje()
                    + ", '" + datosIXP[i].getNombreProducto() + "'"
                    + ", '" + datosIXP[i].getComentario() + "'"
                    + "," + datosIXP[i].getIdInsumo();
            PreparedStatement pstmt = null;
            c.conectar();
            pstmt = c.getConexion().prepareStatement(query);
            System.out.println(query);
            pstmt.executeUpdate();
            c.desconectar();
        }
    }
    
    //Método para traer los insumos
    public List ObtenerInsumos() throws Exception
    {
        List<Insumo> lstInsumo = new ArrayList<>();
        Insumo obj;
        
        String query="execute sp_Compaq_ObtInsumos";
        
        Statement stmt = null;
        ResultSet rs = null;

        c.conectarCompaq();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                obj = new Insumo();
                obj.setCIDPRODUCTO(rs.getInt("CIDPRODUCTO"));
                obj.setCNOMBREPRODUCTO(rs.getString("CNOMBREPRODUCTO"));
                obj.setCNOMBREUNIDAD(rs.getString("CNOMBREUNIDAD"));
                lstInsumo.add(obj);
            }
        }
        rs.close();
        stmt.close();
        c.desconectar();
        return lstInsumo;
    }
}
