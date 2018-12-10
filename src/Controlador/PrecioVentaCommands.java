/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Calibre;
import Modelo.PrecioVenta;
import Modelo.Seleccion;
import Modelo.TipoRecorte;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        int columnas = 7;
        int i = 0;
        
        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            renglones = rs.getRow();
            precioVenta = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                precioVenta[i][0]= rs.getString("tipoRecorte");
                precioVenta[i][1]= rs.getString("calibre");
                precioVenta[i][2]= rs.getString("seleccion");
                precioVenta[i][3]= rs.getString("precio");
                precioVenta[i][4]= rs.getString("descripcion");
                
                Date sqlDate = rs.getDate("fecha");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                precioVenta[i][5] = sdf.format(sqlDate);
                precioVenta[i][6]= rs.getString("idPrecioVenta");
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return precioVenta;
    }
    
    //Método que se llama para insertar un calibre
    public static void insertarPrecioVenta(PrecioVenta pv) throws Exception {
        String query= "execute sp_insPrecioVenta "
                + "" + pv.getIdSeleccion() +","
                + pv.getIdCalibre()+","
                + pv.getIdTipoRecorte()+","
                + pv.getPrecio()+","
                + pv.getUnidadMedida();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para obtener todos los calibres activos en BD
    public static int obtenerPrecioVentaDisp(PrecioVenta pv) throws Exception
    {
        int idPrecioVenta = 0;
        
        String query="execute sp_obtPrecioVentaDisp "
                + pv.getIdSeleccion()
                + "," + pv.getIdCalibre()
                + "," + pv.getIdTipoRecorte();
        
        Statement stmt = null;
        ResultSet rs = null;
        
        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                idPrecioVenta = rs.getInt("idPrecioVenta");
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return idPrecioVenta;
    }
    
    //Método que se llama para actualizar detos de un precio de venta
    public static void actualizarPrecioVenta(PrecioVenta pv) throws Exception {
        String query= "execute sp_actPrecioVenta "
                + "" + pv.getIdPrecioVenta()+ ""
                + ", " + pv.getPrecio();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
