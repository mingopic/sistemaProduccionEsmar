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
public class FichaProdDet {
    int idFichaProdDet;
    int idFichaProd;
    int idPartidaDet;
    int noPiezasTotal;
    Double kgTotal;
    Double costoTotalCuero;
    Double costoInsumos;

    public int getIdFichaProdDet() {
        return idFichaProdDet;
    }

    public void setIdFichaProdDet(int idFichaProdDet) {
        this.idFichaProdDet = idFichaProdDet;
    }

    public int getIdFichaProd() {
        return idFichaProd;
    }

    public void setIdFichaProd(int idFichaProd) {
        this.idFichaProd = idFichaProd;
    }

    public int getIdPartidaDet() {
        return idPartidaDet;
    }

    public void setIdPartidaDet(int idPartidaDet) {
        this.idPartidaDet = idPartidaDet;
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

    public Double getCostoTotalCuero() {
        return costoTotalCuero;
    }

    public void setCostoTotalCuero(Double costoTotalCuero) {
        this.costoTotalCuero = costoTotalCuero;
    }

    public Double getCostoInsumos() {
        return costoInsumos;
    }

    public void setCostoInsumos(Double costoInsumos) {
        this.costoInsumos = costoInsumos;
    }
}
