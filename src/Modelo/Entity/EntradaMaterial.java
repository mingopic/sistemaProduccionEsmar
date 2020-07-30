/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entity;

import Modelo.Dto.EntradaMaterialDto;
import java.util.Date;

/**
 *
 * @author mingo
 */
public class EntradaMaterial extends EntradaMaterialDto{
    int EntradaMaterialId;
    int MaterialId;
    Double Cantidad;
    String Comentarios;
    int idUsuario;
    Date FechaEntrada;
    Date FechaInsercion;

    public int getEntradaMaterialId() {
        return EntradaMaterialId;
    }

    public void setEntradaMaterialId(int EntradaMaterialId) {
        this.EntradaMaterialId = EntradaMaterialId;
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

    public String getComentarios() {
        return Comentarios;
    }

    public void setComentarios(String Comentarios) {
        this.Comentarios = Comentarios;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaEntrada() {
        return FechaEntrada;
    }

    public void setFechaEntrada(Date FechaEntrada) {
        this.FechaEntrada = FechaEntrada;
    }

    public Date getFechaInsercion() {
        return FechaInsercion;
    }

    public void setFechaInsercion(Date FechaInsercion) {
        this.FechaInsercion = FechaInsercion;
    }
    
}
