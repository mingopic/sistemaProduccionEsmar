/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.BajasInventarioCross;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Mingo
 */
public class BajasInventarioCrossCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método para agregar una baja a la tabla tb_bajasInventarioSemiterminado
    public static void agregarBajaInvSemiterminado(BajasInventarioCross bic) throws Exception
    {
        String query = "exec sp_agrBajaInvCross "+bic.getNoPiezas()+""
            + ",'"+bic.getMotivo()+"',"+bic.getIdInvPCross();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
