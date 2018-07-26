/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Usuario;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 *
 * @author Domingo Luna
 */
public class UsuarioCommands
{
    
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
            u.setIdUsuario(rs.getString("idUsuario"));
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
}
