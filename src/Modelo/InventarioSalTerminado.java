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
    double kg;
    double decimetros;
    double pies;
    int bandera;
    int idCalibre;
    int idSeleccion;
    String tipoRecorte;
    String calibre;
    String seleccion;

    public String getCalibre() {
        return calibre;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }

    public String getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(String seleccion) {
        this.seleccion = seleccion;
    }

    public String getTipoRecorte() {
        return tipoRecorte;
    }

    public void setTipoRecorte(String tipoRecorte) {
        this.tipoRecorte = tipoRecorte;
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

    public int getBandera() {
        return bandera;
    }

    public void setBandera(int bandera) {
        this.bandera = bandera;
    }

    public double getDecimetros() {
        return decimetros;
    }

    public void setDecimetros(double decimetros) {
        this.decimetros = decimetros;
    }

    public double getPies() {
        return pies;
    }

    public void setPies(double pies) {
        this.pies = pies;
    }

    public double getKg() {
        return kg;
    }

    public void setKg(double kg) {
        this.kg = kg;
    }

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
