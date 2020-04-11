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
public class CostoGarra {
    int idCostoGarra;
    double costo;
    String fecha;

    public int getIdCostoGarra() {
        return idCostoGarra;
    }

    public void setIdCostoGarra(int idCostoGarra) {
        this.idCostoGarra = idCostoGarra;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
