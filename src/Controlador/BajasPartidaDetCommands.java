/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.BajasPartidaDet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Mingo
 */
public class BajasPartidaDetCommands {
    static Statement stmt = null;
    static PreparedStatement pstmt;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método para agregar una baja a la tabla tb_bajasPartidaDet
    /*
    public static void agregarBajaPartidaDet(BajasPartidaDet bpd) throws Exception
    {
        String query = "exec sp_agrBajaPartidaDet "+bpd.getNoPiezas()+""
            + ",'"+bpd.getMotivo()+"',"+bpd.getIdPartidaDet();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    */
}