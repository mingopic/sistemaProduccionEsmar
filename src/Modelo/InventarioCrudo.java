/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author EQUIPO-PC
 */
public class InventarioCrudo {
    int idInventarioCrudo;
    int noPiezasActual;
    int idRecepcionCuero;
    int idTipoRecorte;
    String recorte;

    public int getIdInventarioCrudo() {
        return idInventarioCrudo;
    }

    public void setIdInventarioCrudo(int idInventarioCrudo) {
        this.idInventarioCrudo = idInventarioCrudo;
    }

    public int getNoPiezasActual() {
        return noPiezasActual;
    }

    public void setNoPiezasActual(int noPiezasActual) {
        this.noPiezasActual = noPiezasActual;
    }

    public int getIdRecepcionCuero() {
        return idRecepcionCuero;
    }

    public void setIdRecepcionCuero(int idRecepcionCuero) {
        this.idRecepcionCuero = idRecepcionCuero;
    }

    public String getRecorte() {
        return recorte;
    }

    public int getIdTipoRecorte() {
        return idTipoRecorte;
    }

    public void setIdTipoRecorte(int idTipoRecorte) {
        this.idTipoRecorte = idTipoRecorte;
    }

    public void setRecorte(String recorte) {
        this.recorte = recorte;
    }
    
    
}
