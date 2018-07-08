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
public class TipoRecorteCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para llenar el combobox con los tipos de cuero existentes
    public static String[][] llenarComboboxTipoRecorte() throws Exception
    {
        String[][] tipoCuero=null;
        
        String query="execute sp_obtTipoRecorte";
        
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
            tipoCuero = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                tipoCuero[i][0]= rs.getString("idTipoRecorte");
                tipoCuero[i][1]= rs.getString("descripcion");
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return tipoCuero;
    }
}
