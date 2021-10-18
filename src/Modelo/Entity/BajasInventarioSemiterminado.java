/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entity;

/**
 *
 * @author Mingo
 */
public class BajasInventarioSemiterminado {
    String fechaBaja;
    String fechaentrada;
    int idBajaInvSemiterminado;
    int idCalibre;
    int idInvSemiterminado;
    int idPartida;
    int idSeleccion;
    int idTipoRecorte;
    double kgTotal;
    String motivo;
    int noPiezas;

    public String getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getFechaentrada() {
        return fechaentrada;
    }

    public void setFechaentrada(String fechaentrada) {
        this.fechaentrada = fechaentrada;
    }

    public int getIdBajaInvSemiterminado() {
        return idBajaInvSemiterminado;
    }

    public void setIdBajaInvSemiterminado(int idBajaInvSemiterminado) {
        this.idBajaInvSemiterminado = idBajaInvSemiterminado;
    }

    public int getIdCalibre() {
        return idCalibre;
    }

    public void setIdCalibre(int idCalibre) {
        this.idCalibre = idCalibre;
    }

    public int getIdInvSemiterminado() {
        return idInvSemiterminado;
    }

    public void setIdInvSemiterminado(int idInvSemiterminado) {
        this.idInvSemiterminado = idInvSemiterminado;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public int getIdSeleccion() {
        return idSeleccion;
    }

    public void setIdSeleccion(int idSeleccion) {
        this.idSeleccion = idSeleccion;
    }

    public int getIdTipoRecorte() {
        return idTipoRecorte;
    }

    public void setIdTipoRecorte(int idTipoRecorte) {
        this.idTipoRecorte = idTipoRecorte;
    }

    public double getKgTotal() {
        return kgTotal;
    }

    public void setKgTotal(double kgTotal) {
        this.kgTotal = kgTotal;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getNoPiezas() {
        return noPiezas;
    }

    public void setNoPiezas(int noPiezas) {
        this.noPiezas = noPiezas;
    }
    
}
