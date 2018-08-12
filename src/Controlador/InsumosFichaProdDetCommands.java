/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.InsumosFichaProdDet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Mingo
 */
public class InsumosFichaProdDetCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para agregar un registro a la tabla tb_InsumoFichaProdDet
    public static void insertarInsumosFichaProdDet(InsumosFichaProdDet ifpd) throws Exception {
        String query = "execute sp_InsInsumosFichaProdDet "
                + ifpd.getIdInsumoFichaProd()
                + ", '" + ifpd.getClave() + "'"
                + ", " + ifpd.getPorcentaje() 
                + ", '" + ifpd.getMaterial() + "'"
                + ", '" + ifpd.getTemperatura() + "'"
                + ", '" + ifpd.getRodar() + "'"
                + ", " + ifpd.getCantidad()
                + ", '" + ifpd.getObservaciones() + "'"
                + ", " + ifpd.getPrecioUnitario()
                + ", " + ifpd.getTotal();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
