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
public class FichaProduccion {
    int idFichaProd;
    int idTambor;
    int noPiezasTotal;
    double kgTotal;
    String fechaCreacion;
    String fecha;
    String fecha1;

    public int getIdFichaProd() {
        return idFichaProd;
    }

    public void setIdFichaProd(int idFichaProd) {
        this.idFichaProd = idFichaProd;
    }

    public int getIdTambor() {
        return idTambor;
    }

    public void setIdTambor(int idTambor) {
        this.idTambor = idTambor;
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

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha1() {
        return fecha1;
    }

    public void setFecha1(String fecha1) {
        this.fecha1 = fecha1;
    }
}
