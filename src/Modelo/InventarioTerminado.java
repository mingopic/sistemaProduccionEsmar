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
public class InventarioTerminado {
    int idInvTerminado;
    int idInvSemTer;
    int idCalibre;
    int idSeleccion;
    int noPiezas;
    int noPiezasActuales;
    double kgTotales;
    String fechaEntrada;

    public int getIdInvTerminado() {
        return idInvTerminado;
    }

    public void setIdInvTerminado(int idInvTerminado) {
        this.idInvTerminado = idInvTerminado;
    }

    public int getIdInvSemTer() {
        return idInvSemTer;
    }

    public void setIdInvSemTer(int idInvSemTer) {
        this.idInvSemTer = idInvSemTer;
    }

    public int getIdCalibre() {
        return idCalibre;
    }

    public void setIdCalibre(int idCalibre) {
        this.idCalibre = idCalibre;
    }

    public int getIdSeleccion() {
        return idSeleccion;
    }

    public void setIdSeleccion(int idSeleccion) {
        this.idSeleccion = idSeleccion;
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

    public double getKgTotales() {
        return kgTotales;
    }

    public void setKgTotales(double kgTotales) {
        this.kgTotales = kgTotales;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }
}