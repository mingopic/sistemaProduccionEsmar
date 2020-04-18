/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entity;

import java.util.Date;

/**
 *
 * @author mingo
 */
public class SalidaMaterial {
    int SalidaMaterialId;
    int MaterialId;
    Double Cantidad;
    String Solicitante;
    String Departamento;
    String Comentarios;
    int idInsumoFichaProd;
    int idUsuario;
    Date FechaSalida;
    Date FechaInsercion;

    public int getSalidaMaterialId() {
        return SalidaMaterialId;
    }

    public void setSalidaMaterialId(int SalidaMaterialId) {
        this.SalidaMaterialId = SalidaMaterialId;
    }

    public int getMaterialId() {
        return MaterialId;
    }

    public void setMaterialId(int MaterialId) {
        this.MaterialId = MaterialId;
    }

    public Double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(Double Cantidad) {
        this.Cantidad = Cantidad;
    }

    public String getSolicitante() {
        return Solicitante;
    }

    public void setSolicitante(String Solicitante) {
        this.Solicitante = Solicitante;
    }

    public String getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(String Departamento) {
        this.Departamento = Departamento;
    }

    public String getComentarios() {
        return Comentarios;
    }

    public void setComentarios(String Comentarios) {
        this.Comentarios = Comentarios;
    }

    public int getIdInsumoFichaProd() {
        return idInsumoFichaProd;
    }

    public void setIdInsumoFichaProd(int idInsumoFichaProd) {
        this.idInsumoFichaProd = idInsumoFichaProd;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaSalida() {
        return FechaSalida;
    }

    public void setFechaSalida(Date FechaSalida) {
        this.FechaSalida = FechaSalida;
    }

    public Date getFechaInsercion() {
        return FechaInsercion;
    }

    public void setFechaInsercion(Date FechaInsercion) {
        this.FechaInsercion = FechaInsercion;
    }
    
    
}
