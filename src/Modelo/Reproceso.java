/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author SISTEMAS
 */
public class Reproceso {
    int noPartida;
    String tipoRecorte;
    int noPiezasActuales;
    String proveedorCamion;
    String area;
    int idPartidaDet;
    int idPartida;
    int idTipoRecorte;
    int idDescontar;

    public int getIdDescontar() {
        return idDescontar;
    }

    public void setIdDescontar(int idDescontar) {
        this.idDescontar = idDescontar;
    }

    public int getIdTipoRecorte() {
        return idTipoRecorte;
    }

    public void setIdTipoRecorte(int idTipoRecorte) {
        this.idTipoRecorte = idTipoRecorte;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public int getIdPartidaDet() {
        return idPartidaDet;
    }

    public void setIdPartidaDet(int idPartidaDet) {
        this.idPartidaDet = idPartidaDet;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

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

    public int getNoPiezasActuales() {
        return noPiezasActuales;
    }

    public void setNoPiezasActuales(int noPiezasActuales) {
        this.noPiezasActuales = noPiezasActuales;
    }

    public String getProveedorCamion() {
        return proveedorCamion;
    }

    public void setProveedorCamion(String proveedorCamion) {
        this.proveedorCamion = proveedorCamion;
    }
}
