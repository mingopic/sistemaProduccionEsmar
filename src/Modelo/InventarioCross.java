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
public class InventarioCross {
    int idInvPCross;
    int idPartidaDet;
    int idPartida;
    int noPiezas;
    int noPiezasActuales;
    String fechaEntrada;
    String fecha;
    String fecha1;

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

    public int getIdInvPCross() {
        return idInvPCross;
    }

    public void setIdInvPCross(int idInvPCross) {
        this.idInvPCross = idInvPCross;
    }

    public int getIdPartidaDet() {
        return idPartidaDet;
    }

    public void setIdPartidaDet(int idPartidaDet) {
        this.idPartidaDet = idPartidaDet;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
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
