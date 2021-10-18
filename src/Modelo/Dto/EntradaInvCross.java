/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Dto;

/**
 *
 * @author mingo
 */
public class EntradaInvCross {
    String FechaEntrada;
    int IdPartida;
    int IdTipoRecorte;
    double KgActual;
    double KgTotal;
    int NoPartida;
    int NoPiezas;
    int NoPiezasActuales;
    String Recorte;

    public String getFechaEntrada() {
        return FechaEntrada;
    }

    public void setFechaEntrada(String FechaEntrada) {
        this.FechaEntrada = FechaEntrada;
    }

    public int getIdPartida() {
        return IdPartida;
    }

    public void setIdPartida(int IdPartida) {
        this.IdPartida = IdPartida;
    }

    public int getIdTipoRecorte() {
        return IdTipoRecorte;
    }

    public void setIdTipoRecorte(int IdTipoRecorte) {
        this.IdTipoRecorte = IdTipoRecorte;
    }

    public double getKgActual() {
        return KgActual;
    }

    public void setKgActual(double KgActual) {
        this.KgActual = KgActual;
    }

    public double getKgTotal() {
        return KgTotal;
    }

    public void setKgTotal(double KgTotal) {
        this.KgTotal = KgTotal;
    }

    public int getNoPartida() {
        return NoPartida;
    }

    public void setNoPartida(int NoPartida) {
        this.NoPartida = NoPartida;
    }

    public int getNoPiezas() {
        return NoPiezas;
    }

    public void setNoPiezas(int NoPiezas) {
        this.NoPiezas = NoPiezas;
    }

    public int getNoPiezasActuales() {
        return NoPiezasActuales;
    }

    public void setNoPiezasActuales(int NoPiezasActuales) {
        this.NoPiezasActuales = NoPiezasActuales;
    }

    public String getRecorte() {
        return Recorte;
    }

    public void setRecorte(String Recorte) {
        this.Recorte = Recorte;
    }

}
