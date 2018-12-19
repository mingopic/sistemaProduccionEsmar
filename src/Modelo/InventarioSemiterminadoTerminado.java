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
    String fecha;
    String fecha1;
    int bandera;

    public int getBandera() {
        return bandera;
    }

    public void setBandera(int bandera) {
        this.bandera = bandera;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha1() {
        return fecha1;
    }

    public void setFecha1(String fecha1) {
        this.fecha1 = fecha1;
    }

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
