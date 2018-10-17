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
public class Partida {
    int idPartida;
    int noPartida;
    int noTotalPiezas;
    String fecha;
    int idProceso;
    String anio;

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public int getNoPartida() {
        return noPartida;
    }

    public void setNoPartida(int noPartida) {
        this.noPartida = noPartida;
    }

    public int getNoTotalPiezas() {
        return noTotalPiezas;
    }

    public void setNoTotalPiezas(int noTotalPiezas) {
        this.noTotalPiezas = noTotalPiezas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }
}
