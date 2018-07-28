/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.InventarioSemiterminadoTerminado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author EQUIPO-PC
 */
public class InventarioSemiterminadoTerminadoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para agregar una entrada a la tabla tb_invSemTer
    public static void agregarInvSemTer(InventarioSemiterminadoTerminado ist, double kgTotales) throws Exception
    {
        String query = "execute sp_agrInvSemTer "+ist.getIdInvSemiterminado()+""
            + ","+ist.getNoPiezas()+","+ist.getNoPiezasActuales()+""
                + ","+kgTotales;
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
