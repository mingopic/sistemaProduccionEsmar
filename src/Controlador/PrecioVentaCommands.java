/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Calibre;
import Modelo.Seleccion;
import Modelo.TipoRecorte;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Mingo
 */
public class PrecioVentaCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para obtener todos los precios de venta en BD
    public static String[][] obtenerTodosPrecioVenta(TipoRecorte tr, Calibre ca, Seleccion s) throws Exception
    {
        String[][] precioVenta=null;
        
        String query="execute sp_obtPrecioVenta "
                + tr.getIdTipoRecorte()
                + "," + ca.getIdCalibre()
                + "," + s.getIdSeleccion();
        
        Statement stmt = null;
        ResultSet rs = null;
        int renglones = 0;
        int columnas = 6;
        int i = 0;
        
        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            renglones = rs.getRow();
            precioVenta = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                precioVenta[i][0]= rs.getString("tipoRecorte");
                precioVenta[i][1]= rs.getString("calibbre");
                precioVenta[i][2]= rs.getString("seleccion");
                precioVenta[i][3]= rs.getString("precio");
                precioVenta[i][4]= rs.getString("fecha");
                precioVenta[i][5]= rs.getString("idPrecioVenta");
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return precioVenta;
    }
}
