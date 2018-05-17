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
 * @author Equipo
 */
public class ProveedorCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para llenar el combobox con los datos de los productos existentes
    public static String[][] llenarComboboxProveedores() throws Exception
    {
        String[][] proveedores=null;
        
        String query="execute sp_obtProv";
        
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
            proveedores = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                proveedores[i][0]= rs.getString("idProveedor");
                proveedores[i][1]= rs.getString("nombreProveedor");
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return proveedores;
    }
}
