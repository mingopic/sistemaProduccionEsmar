/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.ConfPrecioManoDeObra;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mingo
 */
public class ConfPrecioManoDeObraCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método que se llama para obtener las configuraciones de los precios de Mano de obra
    public static List<ConfPrecioManoDeObra> obtenerConfPrecioManoDeObra() 
    {
        List<ConfPrecioManoDeObra> configuraciones = null;
        String query = "execute sp_obtConfPrecioManoDeObra ";

        Statement stmt = null;
        ResultSet rs = null;
        
        try 
        {
            c.conectar();
            stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(query);
            rs = stmt.executeQuery(query);

            configuraciones = new ArrayList<>();
            if (rs.last()) 
            {
                rs.beforeFirst();

                //Recorremos el ResultSet registro a registro
                while (rs.next()) {
                    ConfPrecioManoDeObra cp = new ConfPrecioManoDeObra();
                    cp.setIdConfPrecioManoDeObra(rs.getInt("idConfPrecioManoDeObra"));
                    cp.setIdTipoRecorte(rs.getInt("idTipoRecorte"));
                    cp.setDescTipoRecorte(rs.getString("descTipoRecorte"));
                    cp.setCosto(rs.getDouble("costo"));
                    configuraciones.add(cp);
                }
            }
            rs.close();
            stmt.close();
            c.desconectar();
        } 
        catch (Exception e) 
        {
            System.err.println(e);
        }
        return configuraciones;
    }
}
