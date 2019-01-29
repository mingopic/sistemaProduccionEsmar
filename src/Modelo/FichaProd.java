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
public class FichaProd {
    int idFichaProd;
    int idTambor;
    int noPiezasTotal;
    Double kgTotal;
    Double costoInsumos;
    String fechaCreacion;
    int idSubproceso;

    public int getIdSubproceso() {
        return idSubproceso;
    }

    public void setIdSubproceso(int idSubproceso) {
        this.idSubproceso = idSubproceso;
    }

    public int getIdFichaProd() {
        return idFichaProd;
    }

    public void setIdFichaProd(int idFichaProd) {
        this.idFichaProd = idFichaProd;
    }

    public int getIdTambor() {
        return idTambor;
    }

    public void setIdTambor(int idTambor) {
        this.idTambor = idTambor;
    }

    public int getNoPiezasTotal() {
        return noPiezasTotal;
    }

    public void setNoPiezasTotal(int noPiezasTotal) {
        this.noPiezasTotal = noPiezasTotal;
    }

    public Double getKgTotal() {
        return kgTotal;
    }

    public void setKgTotal(Double kgTotal) {
        this.kgTotal = kgTotal;
    }

    public Double getCostoInsumos() {
        return costoInsumos;
    }

    public void setCostoInsumos(Double costoInsumos) {
        this.costoInsumos = costoInsumos;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
