/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.FichaProd;
import Modelo.Insumo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mingo
 */
public class FichaProdCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para agregar una ficha de producción
    public static void agregarFichaProd(FichaProd fp) throws Exception {
        String query = "execute sp_InsFichaProd "
                + fp.getIdTambor()
                + ", " + fp.getNoPiezasTotal()
                + ", " + fp.getKgTotal()
                + ", " + fp.getCostoInsumos()
                + ", " + fp.getIdSubproceso();
        
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para obtener la última ficha de producción creada
    public int obtenerUltFichaProduccion() throws Exception
    {
        int idFichaProd = 0;
        
        String query="execute sp_ObtUltimaFichaProd";
        
        Statement stmt = null;
        ResultSet rs = null;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                idFichaProd = (rs.getInt("idFichaProd"));
            }
        }
        rs.close();
        stmt.close();
        c.desconectar();
        return idFichaProd;
    }
}
