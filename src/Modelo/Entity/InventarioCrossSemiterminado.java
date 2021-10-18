/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entity;

/**
 *
 * @author EQUIPO-PC
 */
public class InventarioCrossSemiterminado {
    int idInvCrossSemi;
    int idInvPCross;
    int idPartida;
    int idTipoRecorte;
    int noPartida;
    int noPiezas;
    int noPiezasActuales;
    String fechaEntrada;
    String fecha;
    String fecha1;
    double kgTotal;
    double kgActual;
    String recorte;

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

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public int getIdTipoRecorte() {
        return idTipoRecorte;
    }

    public void setIdTipoRecorte(int idTipoRecorte) {
        this.idTipoRecorte = idTipoRecorte;
    }

    public int getNoPartida() {
        return noPartida;
    }

    public void setNoPartida(int noPartida) {
        this.noPartida = noPartida;
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

    public double getKgTotal() {
        return kgTotal;
    }

    public void setKgTotal(double kgTotal) {
        this.kgTotal = kgTotal;
    }

    public double getKgActual() {
        return kgActual;
    }

    public void setKgActual(double kgActual) {
        this.kgActual = kgActual;
    }

    public String getRecorte() {
        return recorte;
    }

    public void setRecorte(String recorte) {
        this.recorte = recorte;
    }
    
}
