/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.RolesXUsuario;
import Modelo.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author EQUIPO-PC
 */
public class RolesXUsuarioCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método que se llama para insertar un rolXUsuario
    public static void insertarRolXUsuario(int idUsuario, String nombreRol) throws Exception {
        String query= "execute sp_insRolXUsu "+idUsuario+", '"+nombreRol+"'";
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para actualizar detos de un tambor
    public static void actualizarRolXUsuario(Usuario u, String nombreRol) throws Exception {
        String query= "execute sp_actRolXUsu "+u.getIdUsuario()+",'"+nombreRol+"'";
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    public static String[] obtenerRolXUsuario(Usuario u) throws Exception
    {
        String[] roles = null;
        
        String query = "execute sp_obtRolXUsu "+u.getIdUsuario();
        
        Statement stmt = null;
        ResultSet rs = null;
        int renglones = 0;
        int i = 0;
        
        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            renglones = rs.getRow();
            roles = new String[renglones];
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                roles[i] = rs.getString("nombreRol");
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return roles;
    }
    
    //Método que se llama para actualizar detos de un tambor
    public static void eliminarRolXUsuario(Usuario u) throws Exception {
        String query= "execute sp_EliRolXUsu "+u.getIdUsuario();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
