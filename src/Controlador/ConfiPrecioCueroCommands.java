/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.ConfPrecioCuero;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Equipo
 */
public class ConfiPrecioCueroCommands  {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método que se llama para obtener las configuraciones de los precios de cueros
    public static List<ConfPrecioCuero> obtenerConfPrecioCuero() 
    {
        List<ConfPrecioCuero> configuraciones = null;
        String query = "execute sp_obtConfPrecioCuero ";

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
                    ConfPrecioCuero cp = new ConfPrecioCuero();
                    cp.setIdConfPrecioCuero(rs.getInt("idConfPrecioCuero"));
                    cp.setIdTipoRecorte(rs.getInt("idTipoRecorte"));
                    cp.setDescTipoRecorte(rs.getString("descTipoRecorte"));
                    cp.setPorcentaje(rs.getDouble("porcentaje"));
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
    
    //Método para actualizar el porcentaje del precio de un tipo de recorte
    public static void actualizarPorcentajePrecio(ConfPrecioCuero cp) throws Exception {
        String query = "exec sp_actConfPrecioCuero "
                + cp.getIdConfPrecioCuero()
                + ", " + cp.getPorcentaje();
        
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
