/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Imagenes;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author EQUIPO-PC
 */
public class ImagenesCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método que se llama para obtener las imagenes de la base de datos
    public static String[][] obtenerImagenes(Imagenes i) throws Exception {
        String query;
        
        query= "EXEC sp_obtImagenes '"+i.getImagen()+"'";

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 2;
        int j = 0;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) 
        {
            renglones = rs.getRow();
            datos = new String[renglones][columnas];
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                datos[j][0] = rs.getString("idImagen");
                datos[j][1] = rs.getString("imagen");
                j++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
}
