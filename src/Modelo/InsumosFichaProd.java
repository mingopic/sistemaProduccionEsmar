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
public class InsumosFichaProd {
    int idInsumoFichaProd;
    int idFichaProd;
    int idProceso;
    int idSubproceso;
    int idFormXSubProc;
    Double totalInsumos;

    public int getIdInsumoFichaProd() {
        return idInsumoFichaProd;
    }

    public void setIdInsumoFichaProd(int idInsumoFichaProd) {
        this.idInsumoFichaProd = idInsumoFichaProd;
    }

    public int getIdFichaProd() {
        return idFichaProd;
    }

    public void setIdFichaProd(int idFichaProd) {
        this.idFichaProd = idFichaProd;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }

    public int getIdSubproceso() {
        return idSubproceso;
    }

    public void setIdSubproceso(int idSubproceso) {
        this.idSubproceso = idSubproceso;
    }

    public int getIdFormXSubProc() {
        return idFormXSubProc;
    }

    public void setIdFormXSubProc(int idFormXSubProc) {
        this.idFormXSubProc = idFormXSubProc;
    }

    public Double getTotalInsumos() {
        return totalInsumos;
    }

    public void setTotalInsumos(Double totalInsumos) {
        this.totalInsumos = totalInsumos;
    }
}
