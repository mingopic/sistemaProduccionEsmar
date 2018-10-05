/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.BajasInventarioSemiterminado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Mingo
 */
public class BajasInventarioSemiterminadoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método para agregar una baja a la tabla tb_bajasInventarioCrudo
    public static void agregarBajaInvSemiterminado(BajasInventarioSemiterminado bis) throws Exception
    {
        String query = "exec sp_agrBajaInvSemiterminado "+bis.getNoPiezas()+""
            + ",'"+bis.getMotivo()+"',"+bis.getIdInvSemiterminado();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
