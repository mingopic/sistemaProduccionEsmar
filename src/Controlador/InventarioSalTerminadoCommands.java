/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.InventarioSalTerminado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author EQUIPO-PC
 */
public class InventarioSalTerminadoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método para agregar una entrada a la tabla tb_invsalTerminado
    public static void agregarInvSalTer(InventarioSalTerminado isalt) throws Exception
    {
        String query = "execute sp_agrInvSalTer "+isalt.getIdInvTerminado()+""
            + ","+isalt.getNoPiezas()+","+isalt.getKg()+","+isalt.getDecimetros()+","+isalt.getPies();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
