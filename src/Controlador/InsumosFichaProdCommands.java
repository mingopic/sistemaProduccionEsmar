/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.InsumosFichaProd;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Mingo
 */
public class InsumosFichaProdCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para obtener el último IdInsumoFichaProd
    public static int obtenerUltIdInsumoFichaProd() throws Exception
    {
        int idInsumoFichaProd = 0;
        
        String query = "execute sp_ObtUltIdInsumoFichaProd";
        
        Statement stmt = null;
        ResultSet rs = null;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            rs.beforeFirst();
            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                idInsumoFichaProd = rs.getInt("idInsumoFichaProd");
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return idInsumoFichaProd;
    }
    
    //Método para agregar un registro a la tabla tb_InsumoFichaProd
    public static void agregarInsumosFichaProd(InsumosFichaProd ifp) throws Exception {
        String query = "execute sp_InsInsumosFichaProd "
                + ifp.getIdFichaProd()
                + ", " + ifp.getIdProceso()
                + ", " + ifp.getIdSubproceso()
                + ", " + ifp.getIdFormXSubProc()
                + ", " + ifp.getTotalInsumos();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
