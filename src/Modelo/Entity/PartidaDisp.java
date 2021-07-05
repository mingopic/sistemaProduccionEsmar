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
public class PartidaDisp {
    int noPartida;
    String tipoRecorte;
    int noPiezasAct;
    int idPartida;
    int idTipoRecorte;
    int idTipoRecorteOrigen;
    Double kgXpieza;
    int idProceso;

    public int getNoPartida() {
        return noPartida;
    }

    public void setNoPartida(int noPartida) {
        this.noPartida = noPartida;
    }

    public String getTipoRecorte() {
        return tipoRecorte;
    }

    public void setTipoRecorte(String tipoRecorte) {
        this.tipoRecorte = tipoRecorte;
    }
    
    public int getIdTipoRecorteOrigen() {
        return idTipoRecorteOrigen;
    }

    public void setIdTipoRecorteOrigen(int idTipoRecorteOrigen) {
        this.idTipoRecorteOrigen = idTipoRecorteOrigen;
    }

    public int getNoPiezasAct() {
        return noPiezasAct;
    }

    public void setNoPiezasAct(int noPiezasAct) {
        this.noPiezasAct = noPiezasAct;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public int getIdTipoRecorte() {
        return idTipoRecorte;
    }

    public void setIdTipoRecorte(int idTipoRecorte) {
        this.idTipoRecorte = idTipoRecorte;
    }


    public Double getKgXpieza() {
        return kgXpieza;
    }

    public void setKgXpieza(Double kgXpieza) {
        this.kgXpieza = kgXpieza;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }
}
