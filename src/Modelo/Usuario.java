/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

/**
 *
 * @author Domingo Luna
 */
public class Usuario
{
    int idUsuario;
    String userName;
    String password;
    String nombre;
    int estatus;

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
    
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
    
    /**
     * Este método sirve para comparar si un usuario es igual a otro.
     * Dos usuarios son iguales si el username y el password tienen
     * el mismo valor.
     * @param o
     * @return 
     */
    public boolean equals(Object o)
    {
        Usuario uTmp = null;
        
        if (o == null)
            return false;
        
        //Comparamos si el objeto o es de tipo Usuario
        if (o instanceof Usuario)
        {
            //Convertimos o en un tipo Usuario
            uTmp = (Usuario) o;
            
            if (uTmp.getUserName() == null || uTmp.getPassword() == null)
                return false;
            
            //Comparamos el userName de esta clase, con el del objeto uTmp y
            //Comparamos el password de esta clase, con el del objeto uTmp
            //Comparamos el estatus de esta clase, para saber si el estatus es activo
            if (userName.equals(uTmp.getUserName()) && 
                password.equals(uTmp.getPassword()) &&
                uTmp.getEstatus() == 1)
            {
                nombre=uTmp.getNombre();
                idUsuario=uTmp.getIdUsuario();
                estatus = uTmp.getEstatus();
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }
}
