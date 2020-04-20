/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Dto;

/**
 *
 * @author mingo
 */
public class RespuestaDto {
    String Mensaje = "";
    int Respuesta = 0;

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String Mensaje) {
        this.Mensaje = Mensaje;
    }

    public int getRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(int Respuesta) {
        this.Respuesta = Respuesta;
    }
    
}
