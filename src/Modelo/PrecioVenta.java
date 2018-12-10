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
public class PrecioVenta {
    int idPrecioVenta;
    int idSeleccion;
    int idCalibre;
    int idTipoRecorte;
    float precio;
    String fecha;
    int unidadMedida;
    String descUnidadMedida;

    public int getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(int unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getIdPrecioVenta() {
        return idPrecioVenta;
    }

    public void setIdPrecioVenta(int idPrecioVenta) {
        this.idPrecioVenta = idPrecioVenta;
    }

    public int getIdSeleccion() {
        return idSeleccion;
    }

    public void setIdSeleccion(int idSeleccion) {
        this.idSeleccion = idSeleccion;
    }

    public int getIdCalibre() {
        return idCalibre;
    }

    public void setIdCalibre(int idCalibre) {
        this.idCalibre = idCalibre;
    }

    public int getIdTipoRecorte() {
        return idTipoRecorte;
    }

    public void setIdTipoRecorte(int idTipoRecorte) {
        this.idTipoRecorte = idTipoRecorte;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescUnidadMedida() {
        return descUnidadMedida;
    }

    public void setDescUnidadMedida(String descUnidadMedida) {
        this.descUnidadMedida = descUnidadMedida;
    }

}
