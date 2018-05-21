/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Proveedor;
import java.sql.PreparedStatement;
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
    
    //Método para obtener todos los proveedores en BD
    public static String[][] obtenerProveedores() throws Exception
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
                proveedores[i][0]= rs.getString("nombreProveedor");
                if (rs.getString("estatus").equals("1"))
                {
                    proveedores[i][1]= "Activo";
                }
                else
                {
                    proveedores[i][1]= "Inactivo";
                }
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return proveedores;
    }
    
    //Método para llenar el traer los proveedores activos
    public static String[][] llenarComboboxProveedores() throws Exception
    {
        String[][] proveedores=null;
        
        String query="execute sp_obtProvAct";
        
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
    
    //Método para buscar datos de un proveedor en BD
    public static Proveedor obtenerProveedorXNombre(Proveedor p) throws Exception
    {   
        String query="execute sp_obtProvXnombre '"+p.getNombreProveedor()+"'";
        
        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.next())
        {
            p.setIdProveedor(Integer.parseInt(rs.getString("idProveedor")));
            p.setNombreProveedor(rs.getString("nombreProveedor"));
            p.setEstatus(Integer.parseInt(rs.getString("estatus")));
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return p;
    }
    
    //Método que se llama para insertar un proveedor
    public static void insertarProveedor(Proveedor p) throws Exception {
        String query= "execute sp_insProv '"+p.getNombreProveedor()+"', "+p.getEstatus();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para actualizar detos de un proveedor
    public static void actualizarProveedor(Proveedor p) throws Exception {
        String query= "execute sp_actProv '"+p.getNombreProveedor()+"',"+p.getEstatus()+","+p.getIdProveedor();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
