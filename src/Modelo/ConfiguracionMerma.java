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
public class ConfiguracionMerma {
    int idConfigMerma;
    int idTipoMerma;
    double porcMermaAcep;
    String fechaConfig;

    public int getIdConfigMerma() {
        return idConfigMerma;
    }

    public void setIdConfigMerma(int idConfigMerma) {
        this.idConfigMerma = idConfigMerma;
    }

    public int getIdTipoMerma() {
        return idTipoMerma;
    }

    public void setIdTipoMerma(int idTipoMerma) {
        this.idTipoMerma = idTipoMerma;
    }

    public double getPorcMermaAcep() {
        return porcMermaAcep;
    }

    public void setPorcMermaAcep(double porcMermaAcep) {
        this.porcMermaAcep = porcMermaAcep;
    }

    public String getFechaConfig() {
        return fechaConfig;
    }

    public void setFechaConfig(String fechaConfig) {
        this.fechaConfig = fechaConfig;
    }
}
