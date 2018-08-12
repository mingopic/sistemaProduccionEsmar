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
public class InsumosFichaProdDet {
    int idIsumoFichaProdDet;
    int idInsumoFichaProd;
    String clave;
    Double porcentaje;
    String material;
    String temperatura;
    String rodar;
    Double cantidad;
    String observaciones;
    Double precioUnitario;
    Double total;

    public int getIdIsumoFichaProdDet() {
        return idIsumoFichaProdDet;
    }

    public void setIdIsumoFichaProdDet(int idIsumoFichaProdDet) {
        this.idIsumoFichaProdDet = idIsumoFichaProdDet;
    }

    public int getIdInsumoFichaProd() {
        return idInsumoFichaProd;
    }

    public void setIdInsumoFichaProd(int idInsumoFichaProd) {
        this.idInsumoFichaProd = idInsumoFichaProd;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String Material) {
        this.material = Material;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String Temperatura) {
        this.temperatura = Temperatura;
    }

    public String getRodar() {
        return rodar;
    }

    public void setRodar(String Rodar) {
        this.rodar = Rodar;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String Observaciones) {
        this.observaciones = Observaciones;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double PrecioUnitario) {
        this.precioUnitario = PrecioUnitario;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
