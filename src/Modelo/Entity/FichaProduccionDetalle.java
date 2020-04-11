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
public class FichaProduccionDetalle {
    int idFichaProdDet;
    int idFichaProd;
    int idPartidaDet;
    int noPiezasTotal;
    double kgTotal;
    double costoTotalCuero;
    double costoInsumos;

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

    public double getKgTotal() {
        return kgTotal;
    }

    public void setKgTotal(double kgTotal) {
        this.kgTotal = kgTotal;
    }

    public double getCostoTotalCuero() {
        return costoTotalCuero;
    }

    public void setCostoTotalCuero(double costoTotalCuero) {
        this.costoTotalCuero = costoTotalCuero;
    }

    public double getCostoInsumos() {
        return costoInsumos;
    }

    public void setCostoInsumos(double costoInsumos) {
        this.costoInsumos = costoInsumos;
    }
}
