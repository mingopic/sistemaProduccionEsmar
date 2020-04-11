/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.Usuario;



/**
 *
 * @author Domingo Luna
 */
public class ControladorUsuario
{
    ConexionBD conexion;
    public ControladorUsuario(ConexionBD conexion)
    {
        this.conexion = conexion;
    }
    
    public boolean validarUsuario(Usuario u) throws Exception
    {
        Usuario uBD = UsuarioCommands.getUsuarioByUserName(u.getUserName(), 
                                                           conexion);
        return u.equals(uBD);
    }
}
