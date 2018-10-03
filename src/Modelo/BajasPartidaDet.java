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
public class BajasPartidaDet {
    int idBajaPartidaDet;
    int noPiezas;
    String motivo;
    String fechaBaja;
    int idPartidaDet;

    public int getIdBajaPartidaDet() {
        return idBajaPartidaDet;
    }

    public void setIdBajaPartidaDet(int idBajaPartidaDet) {
        this.idBajaPartidaDet = idBajaPartidaDet;
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

    public int getIdPartidaDet() {
        return idPartidaDet;
    }

    public void setIdPartidaDet(int idPartidaDet) {
        this.idPartidaDet = idPartidaDet;
    }
}
