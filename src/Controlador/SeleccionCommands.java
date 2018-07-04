/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author EQUIPO-PC
 */
public class SeleccionCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para llenar el combobox con los calibres existentes
    public static String[][] llenarComboboxSeleccion() throws Exception
    {
        String[][] seleccion=null;
        
        String query="execute sp_obtSeleccion";
        
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
            seleccion = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                seleccion[i][0]= rs.getString("idSeleccion");
                seleccion[i][1]= rs.getString("descripcion");
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return seleccion;
    }
}
