/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entity;

/**
 *
 * @author Mingo
 */
public class BajasInventarioCross {
    int idBajaInvCross;
    int noPiezas;
    String motivo;
    String fechaBaja;
    int idInvPCross;
    double kgTotal;

    public double getKgTotal() {
        return kgTotal;
    }

    public void setKgTotal(double kgTotal) {
        this.kgTotal = kgTotal;
    }

    public int getIdBajaInvCross() {
        return idBajaInvCross;
    }

    public void setIdBajaInvCross(int idBajaInvCross) {
        this.idBajaInvCross = idBajaInvCross;
    }

    public int getNoPiezas() {
        return noPiezas;
    }

    public void setNoPiezas(int noPiezas) {
        this.noPiezas = noPiezas;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public int getIdInvPCross() {
        return idInvPCross;
    }

    public void setIdInvPCross(int idInvPCross) {
        this.idInvPCross = idInvPCross;
    }
}
