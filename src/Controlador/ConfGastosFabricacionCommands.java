/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.ConfGastosFabricacion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author EQUIPO-PC
 */
public class ConfGastosFabricacionCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método que se llama para obtener las configuraciones de los gastos de fabricación
    public static List<ConfGastosFabricacion> obtenerConfGastosFabricacion() 
    {
        List<ConfGastosFabricacion> configuraciones = null;
        String query = "execute sp_obtConfGastosFabricacion ";

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
                    ConfGastosFabricacion cg = new ConfGastosFabricacion();
                    cg.setIdConfGastosFabricacion(rs.getInt("idConfGastosFabricacion"));
                    cg.setIdTipoRecorte(rs.getInt("idTipoRecorte"));
                    cg.setDescTipoRecorte(rs.getString("descTipoRecorte"));
                    cg.setCosto(Double.parseDouble(String.format("%.2f",rs.getDouble("costo"))));
                    configuraciones.add(cg);
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
    public static void actualizarCosto(ConfGastosFabricacion cgf) throws Exception {
        String query = "exec sp_actConfGastosFabricacion "
                + cgf.getIdConfGastosFabricacion()
                + ", " + cgf.getCosto();
        
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
