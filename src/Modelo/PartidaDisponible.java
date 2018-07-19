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
public class PartidaDisponible {
    int noPartida;
    String recorte;
    int noPiezasAct;
    int idPartidaDet;
    int idPartida;
    int idTipoRecorte;

    public int getNoPartida() {
        return noPartida;
    }

    public void setNoPartida(int noPartida) {
        this.noPartida = noPartida;
    }

    public String getRecorte() {
        return recorte;
    }

    public void setRecorte(String recorte) {
        this.recorte = recorte;
    }

    public int getNoPiezasAct() {
        return noPiezasAct;
    }

    public void setNoPiezasAct(int noPiezasAct) {
        this.noPiezasAct = noPiezasAct;
    }

    public int getIdPartidaDet() {
        return idPartidaDet;
    }

    public void setIdPartidaDet(int idPartidaDet) {
        this.idPartidaDet = idPartidaDet;
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
}
