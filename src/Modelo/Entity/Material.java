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

    public int getMaterialId() {
        return MaterialId;
    }

    public void setMaterialId(int MaterialId) {
        this.MaterialId = MaterialId;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public Double getExistencia() {
        return Existencia;
    }

    public void setExistencia(Double Existencia) {
        this.Existencia = Existencia;
    }

    public int getIdUnidadMedida() {
        return idUnidadMedida;
    }

    public void setIdUnidadMedida(int idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
    }

    public Double getPrecio() {
        return Precio;
    }

    public void setPrecio(Double Precio) {
        this.Precio = Precio;
    }

    public int getIdTipoMoneda() {
        return idTipoMoneda;
    }

    public void setIdTipoMoneda(int idTipoMoneda) {
        this.idTipoMoneda = idTipoMoneda;
    }

    public int getCatDetTipoMaterialId() {
        return CatDetTipoMaterialId;
    }

    public void setCatDetTipoMaterialId(int CatDetTipoMaterialId) {
        this.CatDetTipoMaterialId = CatDetTipoMaterialId;
    }

    public int getCatDetClasificacionId() {
        return CatDetClasificacionId;
    }

    public void setCatDetClasificacionId(int CatDetClasificacionId) {
        this.CatDetClasificacionId = CatDetClasificacionId;
    }

    public int getCatDetEstatusId() {
        return CatDetEstatusId;
    }

    public void setCatDetEstatusId(int CatDetEstatusId) {
        this.CatDetEstatusId = CatDetEstatusId;
    }

    public String getFechaUltimaAct() {
        return FechaUltimaAct;
    }

    public void setFechaUltimaAct(String FechaUltimaAct) {
        this.FechaUltimaAct = FechaUltimaAct;
    }

}
