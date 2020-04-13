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

    public int CatDetId() {
        return CatDetId;
    }

    public void CatDetId(int CatDetId) {
        this.CatDetId = CatDetId;
    }

    public String Nombre() {
        return Nombre;
    }

    public void Nombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String Abreviacion() {
        return Abreviacion;
    }

    public void Abreviacion(String Abreviacion) {
        this.Abreviacion = Abreviacion;
    }

    public int OrdenVisualizacion() {
        return OrdenVisualizacion;
    }

    public void OrdenVisualizacion(int OrdenVisualizacion) {
        this.OrdenVisualizacion = OrdenVisualizacion;
    }

    public String Auxiliar() {
        return Auxiliar;
    }

    public void Auxiliar(String Auxiliar) {
        this.Auxiliar = Auxiliar;
    }

    public Date FechaUltimaAct() {
        return FechaUltimaAct;
    }

    public void FechaUltimaAct(Date FechaUltimaAct) {
        this.FechaUltimaAct = FechaUltimaAct;
    }

    public int CatId() {
        return CatId;
    }

    public void CatId(int CatId) {
        this.CatId = CatId;
    }
    
    
}
