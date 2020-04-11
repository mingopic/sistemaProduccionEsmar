/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.ConfGastosFabricacion;
import Modelo.Entity.TipoMoneda;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SISTEMAS
 */
public class TipoMonedaCommands {
    static PreparedStatement pstmt;
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para obtener todos los tipos de monedas en BD
    public static List<TipoMoneda> obtenerMonedas() 
    {
        List<TipoMoneda> lstTipoMonedas = null;
        String query = "select * from tb_tipoMoneda;";

        Statement stmt = null;
        ResultSet rs = null;
        
        try 
        {
            c.conectar();
            stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(query);
            rs = stmt.executeQuery(query);

            lstTipoMonedas = new ArrayList<>();
            if (rs.last()) 
            {
                rs.beforeFirst();

                //Recorremos el ResultSet registro a registro
                while (rs.next()) {
                    TipoMoneda tp = new TipoMoneda();
                    tp.setIdTipoMoneda(rs.getInt("idTipoMoneda"));
                    tp.setDescripcion(rs.getString("descripcion"));
                    tp.setAbreviacion(rs.getString("abreviacion"));
                    tp.setTipoCambio(Double.parseDouble(String.format("%.2f",rs.getDouble("tipoCambio"))));
                    lstTipoMonedas.add(tp);
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
        return lstTipoMonedas;
    }
    
    //Método que se llama para actualizar el tipo de cambio
    public static void actualizarTipoCambio(Double valor) throws Exception {
        String query= "execute sp_actTipoCambio "+valor+";";
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
