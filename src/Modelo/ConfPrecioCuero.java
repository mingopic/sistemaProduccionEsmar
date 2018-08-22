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
public class ConfPrecioCuero {
    int idConfPrecioCuero;
    int idTipoRecorte;
    String descTipoRecorte;
    Double porcentaje;

    public int getIdConfPrecioCuero() {
        return idConfPrecioCuero;
    }

    public void setIdConfPrecioCuero(int idConfPrecioCuero) {
        this.idConfPrecioCuero = idConfPrecioCuero;
    }

    public int getIdTipoRecorte() {
        return idTipoRecorte;
    }

    public void setIdTipoRecorte(int idTipoRecorte) {
        this.idTipoRecorte = idTipoRecorte;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getDescTipoRecorte() {
        return descTipoRecorte;
    }

    public void setDescTipoRecorte(String descTipoRecorte) {
        this.descTipoRecorte = descTipoRecorte;
    }
}
