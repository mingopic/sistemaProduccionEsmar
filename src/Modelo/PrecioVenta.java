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
    float precio_original;
    float precio_buffed;
    int idTipoMoneda;
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

    public float getPrecio_original() {
        return precio_original;
    }

    public void setPrecio_original(float precio_original) {
        this.precio_original = precio_original;
    }

    public int getIdTipoMoneda() {
        return idTipoMoneda;
    }

    public void setIdTipoMoneda(int idTipoMoneda) {
        this.idTipoMoneda = idTipoMoneda;
    }

    public float getPrecio_buffed() {
        return precio_buffed;
    }

    public void setPrecio_buffed(float precio_buffed) {
        this.precio_buffed = precio_buffed;
    }

    
}
