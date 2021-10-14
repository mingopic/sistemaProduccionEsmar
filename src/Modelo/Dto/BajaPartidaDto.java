/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Dto;
/**
 *
 * @author Mingo
 */
public class BajaPartidaDto {
    int idPartida;
    int idProceso;
    int idTipoRecorte;
    String motivoBaja;
    int piezasUtilizar;

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }

    public int getIdTipoRecorte() {
        return idTipoRecorte;
    }

    public void setIdTipoRecorte(int idTipoRecorte) {
        this.idTipoRecorte = idTipoRecorte;
    }

    public String getMotivoBaja() {
        return motivoBaja;
    }

    public void setMotivoBaja(String motivoBaja) {
        this.motivoBaja = motivoBaja;
    }

    public int getPiezasUtilizar() {
        return piezasUtilizar;
    }

    public void setPiezasUtilizar(int piezasUtilizar) {
        this.piezasUtilizar = piezasUtilizar;
    }

}
