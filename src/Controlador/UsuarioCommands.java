/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 *
 * @author Domingo Luna
 */
public class UsuarioCommands
{
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    public static Usuario getUsuarioByUserName(String userName, ConexionBD c) throws Exception
    {
        Usuario u = new Usuario();
        Statement stmt = null;
        ResultSet rs = null;
        String query = "EXEC sp_valUsulog '"+userName+"';";
        c.conectar();
        stmt = c.getConexion().createStatement();
        rs = stmt.executeQuery(query);
        
        if (rs.next())
        {
            u.setIdUsuario(Integer.parseInt(rs.getString("idUsuario")));
            u.setUserName(rs.getString("usuario"));
            u.setPassword(rs.getString("contrasenia"));
            u.setNombre(rs.getString("nombre"));
            u.setEstatus(Integer.parseInt(rs.getString("estatus")));
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return u;
    }
    
    //Método para obtener todos los usuarios en BD
    public static String[][] obtenerUsuarios() throws Exception
    {
        String[][] usuarios=null;
        
        String query="execute sp_obtUsuarios";
        
        Statement stmt = null;
        ResultSet rs = null;
        int renglones = 0;
        int columnas = 3;
        int i = 0;
        
        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            renglones = rs.getRow();
            usuarios = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                usuarios[i][0]= rs.getString("usuario");
                usuarios[i][1]= rs.getString("nombre");
                if (rs.getString("estatus").equals("1"))
                {
                    usuarios[i][2]= "Activo";
                }
                else
                {
                    usuarios[i][2]= "Inactivo";
                }
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return usuarios;
    }
    
    //Método para buscar datos de un usuario en BD
    public static Usuario obtenerUsuarioXNombre(Usuario u) throws Exception
    {   
        String query="execute sp_obtUsuXnombre '"+u.getUserName()+"'";
        
        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.next())
        {
            u.setIdUsuario(Integer.parseInt(rs.getString("idUsuario")));
            u.setUserName(rs.getString("usuario"));
            u.setPassword(rs.getString("contrasenia"));
            u.setNombre(rs.getString("nombre"));
            u.setEstatus(Integer.parseInt(rs.getString("estatus")));
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return u;
    }
    
    //Método que se llama para insertar un usuario
    public static void insertarUsuario(Usuario u) throws Exception {
        String query= "execute sp_insUsu '"+u.getUserName()+"', '"+u.getPassword()+""
                + "', '"+u.getNombre()+"', "+u.getEstatus();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para insertar un usuario
    public static int obtenerUltimoId() throws Exception
    {
        int usuarios=0;
        
        String query= "execute sp_obtUltUsu";
        
        Statement stmt = null;
        ResultSet rs = null;
        
        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                usuarios = rs.getInt("idUsuario");
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return usuarios;
    }
    
    public static void actualizarUsuario(Usuario u) throws Exception {
        String query= "execute sp_actUsu '"+u.getUserName()+"','"+u.getPassword()+"','"+u.getNombre()+"',"+u.getEstatus()
                +","+u.getIdUsuario();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
