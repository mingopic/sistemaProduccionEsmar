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
public class InventarioSalTerminado {
    int idInvSalTerminado;
    int idInvTerminado;
    int noPiezas;
    String fechaEntrada;

    public int getIdInvSalTerminado() {
        return idInvSalTerminado;
    }

    public void setIdInvSalTerminado(int idInvSalTerminado) {
        this.idInvSalTerminado = idInvSalTerminado;
    }

    public int getIdInvTerminado() {
        return idInvTerminado;
    }

    public void setIdInvTerminado(int idInvTerminado) {
        this.idInvTerminado = idInvTerminado;
    }

    public int getNoPiezas() {
        return noPiezas;
    }

    public void setNoPiezas(int noPiezas) {
        this.noPiezas = noPiezas;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }
}
