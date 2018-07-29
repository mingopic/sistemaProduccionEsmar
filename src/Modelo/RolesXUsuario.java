/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author EQUIPO-PC
 */
public class RolesXUsuario {
    int idRolXUsuario;
    int idUsuario;
    String nombreRol;

    public int getIdRolXUsuario() {
        return idRolXUsuario;
    }

    public void setIdRolXUsuario(int idRolXUsuario) {
        this.idRolXUsuario = idRolXUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}
