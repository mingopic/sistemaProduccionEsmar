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
public class InventarioSemiterminadoTerminado {
    int idInvSemTer;
    int idInvSemiterminado;
    int noPiezas;
    int noPiezasActuales;
    double kgTotales;
    String fechaEntrada;

    public int getIdInvSemTer() {
        return idInvSemTer;
    }

    public void setIdInvSemTer(int idInvSemTer) {
        this.idInvSemTer = idInvSemTer;
    }

    public int getIdInvSemiterminado() {
        return idInvSemiterminado;
    }

    public void setIdInvSemiterminado(int idInvSemiterminado) {
        this.idInvSemiterminado = idInvSemiterminado;
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
