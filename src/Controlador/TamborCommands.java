/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Tambor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author EQUIPO-PC
 */
public class TamborCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para obtener todos los tambores en BD
    public static String[][] obtenerTambores() throws Exception
    {
        String[][] tambores=null;
        
        String query="execute sp_obtTamb";
        
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
            tambores = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                tambores[i][0]= rs.getString("nombreTambor");
                if (rs.getString("estatus").equals("1"))
                {
                    tambores[i][1]= "Activo";
                }
                else
                {
                    tambores[i][1]= "Inactivo";
                }
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return tambores;
    }
    
    //Método para traer los tambores activos
    public List llenarComboboxTambores() throws Exception
    {
        List<Tambor> lstTambores = new ArrayList<>();
        Tambor obj;
        
        String query="execute sp_obtTambAct";
        
        Statement stmt = null;
        ResultSet rs = null;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                obj = new Tambor();
                obj.setIdTambor(rs.getInt("idTambor"));
                obj.setNombreTambor(rs.getString("nombreTambor"));
                lstTambores.add(obj);
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return lstTambores;
    }
    
    //Método para buscar datos de un tambor en BD
    public static Tambor obtenerTamborXNombre(Tambor t) throws Exception
    {   
        String query="execute sp_obtTambXnombre '"+t.getNombreTambor()+"'";
        
        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.next())
        {
            t.setIdTambor(Integer.parseInt(rs.getString("idTambor")));
            t.setNombreTambor(rs.getString("nombreTambor"));
            t.setEstatus(Integer.parseInt(rs.getString("estatus")));
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return t;
    }
    
    //Método que se llama para insertar un tambor
    public static void insertarTambor(Tambor t) throws Exception {
        String query= "execute sp_insTamb '"+t.getNombreTambor()+"', "+t.getEstatus();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para actualizar detos de un tambor
    public static void actualizarTambor(Tambor t) throws Exception {
        String query= "execute sp_actTamb '"+t.getNombreTambor()+"',"+t.getEstatus()+","+t.getIdTambor();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
