/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Dto;

/**
 *
 * @author Mingo
 */
public class MaterialDto {
    String UnidadMedida;
    String TipoMoneda;
    String TipoMaterial;
    String Clasificacion;
    String Estatus;

    public String UnidadMedida() {
        return UnidadMedida;
    }

    public void UnidadMedida(String UnidadMedida) {
        this.UnidadMedida = UnidadMedida;
    }

    public String TipoMoneda() {
        return TipoMoneda;
    }

    public void TipoMoneda(String TipoMoneda) {
        this.TipoMoneda = TipoMoneda;
    }

    public String TipoMaterial() {
        return TipoMaterial;
    }

    public void TipoMaterial(String TipoMaterial) {
        this.TipoMaterial = TipoMaterial;
    }

    public String Clasificacion() {
        return Clasificacion;
    }

    public void Clasificacion(String Clasificacion) {
        this.Clasificacion = Clasificacion;
    }

    public String Estatus() {
        return Estatus;
    }

    public void Estatus(String ActivoText) {
        this.Estatus = ActivoText;
    }
}
