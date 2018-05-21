/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Equipo
 */
public class RangoPesoCuero {
    int idRangoPesoCuero;
    float rangoMin;
    float rangoMax;
    String fechaConfig;

    public int getIdRangoPesoCuero() {
        return idRangoPesoCuero;
    }

    public void setIdRangoPesoCuero(int idRangoPesoCuero) {
        this.idRangoPesoCuero = idRangoPesoCuero;
    }

    public float getRangoMin() {
        return rangoMin;
    }

    public void setRangoMin(float rangoMin) {
        this.rangoMin = rangoMin;
    }

    public float getRangoMax() {
        return rangoMax;
    }

    public void setRangoMax(float rangoMax) {
        this.rangoMax = rangoMax;
    }

    public String getFechaConfig() {
        return fechaConfig;
    }

    public void setFechaConfig(String fechaConfig) {
        this.fechaConfig = fechaConfig;
    }
}
