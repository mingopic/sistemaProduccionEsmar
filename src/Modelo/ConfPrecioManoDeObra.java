/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Mingo
 */
public class ConfPrecioManoDeObra {
    int idConfPrecioManoDeObra;
    int idTipoRecorte;
    double costo;
    String fecha;
    String descTipoRecorte;

    public String getDescTipoRecorte() {
        return descTipoRecorte;
    }

    public void setDescTipoRecorte(String descTipoRecorte) {
        this.descTipoRecorte = descTipoRecorte;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdConfPrecioManoDeObra() {
        return idConfPrecioManoDeObra;
    }

    public void setIdConfPrecioManoDeObra(int idConfPrecioManoDeObra) {
        this.idConfPrecioManoDeObra = idConfPrecioManoDeObra;
    }

    public int getIdTipoRecorte() {
        return idTipoRecorte;
    }

    public void setIdTipoRecorte(int idTipoRecorte) {
        this.idTipoRecorte = idTipoRecorte;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}
