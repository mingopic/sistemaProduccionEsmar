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
public class InventarioCrossSemiterminado {
    int idInvCrossSemi;
    int idInvPCross;
    int noPiezas;
    int noPiezasActuales;
    String fechaEntrada;

    public int getIdInvCrossSemi() {
        return idInvCrossSemi;
    }

    public void setIdInvCrossSemi(int idInvCrossSemi) {
        this.idInvCrossSemi = idInvCrossSemi;
    }

    public int getIdInvPCross() {
        return idInvPCross;
    }

    public void setIdInvPCross(int idInvPCross) {
        this.idInvPCross = idInvPCross;
    }

    public int getNoPiezas() {
        return noPiezas;
    }

    public void setNoPiezas(int noPiezas) {
        this.noPiezas = noPiezas;
    }

    public int getNoPiezasActuales() {
        return noPiezasActuales;
    }

    public void setNoPiezasActuales(int noPiezasActuales) {
        this.noPiezasActuales = noPiezasActuales;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }
}
