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
public class InventarioSemiterminado {
    int idInvSemiterminado;
    int idInvCrossSemi;
    int idCalibre;
    int idSeleccion;
    double kgTotales;
    String fechaEntrada;
    String fecha;
    String fecha1;

    public int getIdInvSemiterminado() {
        return idInvSemiterminado;
    }

    public void setIdInvSemiterminado(int idInvSemiterminado) {
        this.idInvSemiterminado = idInvSemiterminado;
    }

    public int getIdInvCrossSemi() {
        return idInvCrossSemi;
    }

    public void setIdInvCrossSemi(int idInvCrossSemi) {
        this.idInvCrossSemi = idInvCrossSemi;
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
}
