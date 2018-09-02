/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Calibre;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author EQUIPO-PC
 */
public class CalibreCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para llenar el combobox con los calibres existentes
    public static String[][] llenarComboboxCalibre() throws Exception
    {
        String[][] calibre=null;
        
        String query="execute sp_obtCalibre";
        
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
            calibre = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                calibre[i][0]= rs.getString("idCalibre");
                calibre[i][1]= rs.getString("descripcion");
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return calibre;
    }
    
    //Método para obtener todos los calibres activos en BD
    public static String[][] obtenerCalibres() throws Exception
    {
        String[][] calibres=null;
        
        String query="execute sp_obtCalibre";
        
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
            calibres = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                calibres[i][0]= rs.getString("descripcion");
                if (rs.getString("estatus").equals("1"))
                {
                    calibres[i][1]= "Activo";
                }
                else
                {
                    calibres[i][1]= "Inactivo";
                }
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return calibres;
    }
    
    //Método para obtener todos los calibres en BD
    public static String[][] obtenerTodosCalibres() throws Exception
    {
        String[][] calibres=null;
        
        String query="execute sp_obtCalibre 1";
        
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
            calibres = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                calibres[i][0]= rs.getString("descripcion");
                if (rs.getString("estatus").equals("1"))
                {
                    calibres[i][1]= "Activo";
                }
                else
                {
                    calibres[i][1]= "Inactivo";
                }
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return calibres;
    }
    
    //Método para buscar datos de un calibre en BD
    public static Calibre obtenerCalibreXNombre(Calibre ca) throws Exception
    {   
        String query="execute sp_obtCalXnombre '"+ca.getDescripcion()+"'";
        
        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.next())
        {
            ca.setIdCalibre(Integer.parseInt(rs.getString("idCalibre")));
            ca.setDescripcion(rs.getString("descripcion"));
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return ca;
    }
    
    //Método que se llama para insertar un calibre
    public static void insertarCalibre(Calibre ca) throws Exception {
        String query= "execute sp_insCal "
                + "'" + ca.getDescripcion() +"' ,"
                + ca.getEstatus();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para actualizar detos de un calibre
    public static void actualizarCalibre(Calibre ca) throws Exception {
        String query= "execute sp_actCal "
                + "'" + ca.getDescripcion() + "'"
                + ", " + ca.getEstatus() 
                + ", " + ca.getIdCalibre();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
