/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Entity;

import Modelo.Dto.MaterialDto;

/**
 *
 * @author Mingo
 */
public class Material extends MaterialDto {
    int MaterialId;
    String Codigo;    
    String Descripcion;
    Double Existencia;
    int idUnidadMedida;
    Double Precio;
    int idTipoMoneda;
    int CatDetTipoMaterialId;
    int CatDetClasificacionId;
    int CatDetEstatusId;
    String FechaUltimaAct;

    public int MaterialId() {
        return MaterialId;
    }

    public void MaterialId(int MaterialId) {
        this.MaterialId = MaterialId;
    }

    public String Codigo() {
        return Codigo;
    }

    public void Codigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public String Descripcion() {
        return Descripcion;
    }

    public void Descripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public Double Existencia() {
        return Existencia;
    }

    public void Existencia(Double Existencia) {
        this.Existencia = Existencia;
    }

    public int IdUnidadMedida() {
        return idUnidadMedida;
    }

    public void IdUnidadMedida(int idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
    }

    public Double Precio() {
        return Precio;
    }

    public void Precio(Double Precio) {
        this.Precio = Precio;
    }

    public int IdTipoMoneda() {
        return idTipoMoneda;
    }

    public void IdTipoMoneda(int idTipoMoneda) {
        this.idTipoMoneda = idTipoMoneda;
    }

    public int CatDetTipoMaterialId() {
        return CatDetTipoMaterialId;
    }

    public void CatDetTipoMaterialId(int CatDetTipoMaterialId) {
        this.CatDetTipoMaterialId = CatDetTipoMaterialId;
    }

    public int CatDetClasificacionId() {
        return CatDetClasificacionId;
    }

    public void CatDetClasificacionId(int CatDetClasificacionId) {
        this.CatDetClasificacionId = CatDetClasificacionId;
    }

    public int CatDetEstatusId() {
        return CatDetEstatusId;
    }

    public void CatDetEstatusId(int Activo) {
        this.CatDetEstatusId = Activo;
    }

    public String FechaUltimaAct() {
        return FechaUltimaAct;
    }

    public void FechaUltimaAct(String FechaUltimaAct) {
        this.FechaUltimaAct = FechaUltimaAct;
    }  
}
