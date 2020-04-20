/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entity;

import java.util.Date;

/**
 *
 * @author mingo
 */
public class CatalogoDet {
    int CatDetId;
    String Nombre;
    String Abreviacion;
    int OrdenVisualizacion;
    String Auxiliar;
    Date FechaUltimaAct;
    int CatId;

    public int getCatDetId() {
        return CatDetId;
    }

    public void setCatDetId(int CatDetId) {
        this.CatDetId = CatDetId;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getAbreviacion() {
        return Abreviacion;
    }

    public void setAbreviacion(String Abreviacion) {
        this.Abreviacion = Abreviacion;
    }

    public int getOrdenVisualizacion() {
        return OrdenVisualizacion;
    }

    public void setOrdenVisualizacion(int OrdenVisualizacion) {
        this.OrdenVisualizacion = OrdenVisualizacion;
    }

    public String getAuxiliar() {
        return Auxiliar;
    }

    public void setAuxiliar(String Auxiliar) {
        this.Auxiliar = Auxiliar;
    }

    public Date getFechaUltimaAct() {
        return FechaUltimaAct;
    }

    public void setFechaUltimaAct(Date FechaUltimaAct) {
        this.FechaUltimaAct = FechaUltimaAct;
    }

    public int getCatId() {
        return CatId;
    }

    public void setCatId(int CatId) {
        this.CatId = CatId;
    }
    
    
}
