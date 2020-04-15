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
public class EntradaMaterial {
  int EntradaMaterialId;
  int MaterialId;
  Double Cantidad;
  String Comentarios;
  int idUsuario;
  Date FechaEntrada;
  Date FechaInsercion;

    public int EntradaMaterialId() {
        return EntradaMaterialId;
    }

    public void EntradaMaterialId(int EntradaMaterialId) {
        this.EntradaMaterialId = EntradaMaterialId;
    }

    public int MaterialId() {
        return MaterialId;
    }

    public void MaterialId(int MaterialId) {
        this.MaterialId = MaterialId;
    }

    public Double Cantidad() {
        return Cantidad;
    }

    public void Cantidad(Double Cantidad) {
        this.Cantidad = Cantidad;
    }

    public String Comentarios() {
        return Comentarios;
    }

    public void Comentarios(String Comentarios) {
        this.Comentarios = Comentarios;
    }

    public int IdUsuario() {
        return idUsuario;
    }

    public void IdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date FechaEntrada() {
        return FechaEntrada;
    }

    public void FechaEntrada(Date FechaEntrada) {
        this.FechaEntrada = FechaEntrada;
    }

    public Date FechaInsercion() {
        return FechaInsercion;
    }

    public void FechaInsercion(Date FechaInsercion) {
        this.FechaInsercion = FechaInsercion;
    }
  
  
}
