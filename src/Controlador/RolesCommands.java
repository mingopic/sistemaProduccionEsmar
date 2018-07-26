/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Usuario;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author EQUIPO-PC
 */
public class RolesCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para obtener los roles que pertenecen al usuario
    public static String[] obtenerRolXUsuario(Usuario u) throws Exception
    {
        String[] roles=null;
        
        String query="execute sp_obtRolXUsu "+u.getIdUsuario();
        
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
}
