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
public class InsumoPorProceso {
    int idInsumXProc;
    int idFormXSubProc;
    String clave;
    float porcentaje;
    int idInsumo;

    public int getIdInsumXProc() {
        return idInsumXProc;
    }

    public void setIdInsumXProc(int idInsumXProc) {
        this.idInsumXProc = idInsumXProc;
    }

    public int getIdFormXSubProc() {
        return idFormXSubProc;
    }

    public void setIdFormXSubProc(int idFormXSubProc) {
        this.idFormXSubProc = idFormXSubProc;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }

    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }
}
