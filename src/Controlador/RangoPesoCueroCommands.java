/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.RecepcionCuero;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Equipo
 */
public class RangoPesoCueroCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para señalar el rango minimo y el rango maximo del peso de cuero
    public static String[][] llenarLabelRangoPesoCuero() throws Exception
    {
        String[][] rangoPesoCuero=null;
        
        String query="execute sp_obtRangoPesoCuero";
        
        Statement stmt = null;
        ResultSet rs = null;
        int renglones = 0;
        int columnas = 2;
        int i = 0;
        
        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            renglones = rs.getRow();
            rangoPesoCuero = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                rangoPesoCuero[i][0]= rs.getString("rangoMin");
                rangoPesoCuero[i][1]= rs.getString("rangoMax");
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return rangoPesoCuero;
    }
}
