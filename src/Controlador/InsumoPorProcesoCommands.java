/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.InsumoPorProceso;
import Modelo.SubProceso;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
            String query = "exec sp_agrInsumXProc "+idSubProceso+","+datosIXP[i].getClave()+""
                + ","+datosIXP[i].getPorcentaje()+","+datosIXP[i].getIdInsumo();
            PreparedStatement pstmt = null;
            c.conectar();
            pstmt = c.getConexion().prepareStatement(query);
            System.out.println(query);
            pstmt.executeUpdate();
            c.desconectar();
        }
    }
}
