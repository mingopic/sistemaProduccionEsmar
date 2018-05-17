/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Equipo
 */
public class SubProceso {
    int idSubProceso;
    int idProceso;
    String descripcion;

    public int getIdSubProceso() {
        return idSubProceso;
    }

    public void setIdSubProceso(int idSubProceso) {
        this.idSubProceso = idSubProceso;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
