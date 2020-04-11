/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.BajasInventarioTerminado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Mingo
 */
public class BajasInventarioTerminadoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método para agregar una baja a la tabla tb_bajasInvTerminado
    public static void agregarBajaInvTerminado(BajasInventarioTerminado bit) throws Exception
    {
        String query = "exec sp_agrBajaInvTerminado "
                + bit.getNoPiezas()
                + ", '" + bit.getMotivo() + "'"
                + ", " + bit.getIdInvTerminado()
                + ", " + bit.getBandera();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
