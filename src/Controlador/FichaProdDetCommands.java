/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.FichaProdCommands.c;
import Modelo.FichaProdDet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;

/**
 *
 * @author Mingo
 */
public class FichaProdDetCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para agregar el detalle de una ficha de producción
    public static void agregarFichaProdDet(FichaProdDet fpd, int noPiezasPartida, Double kgPartida, Double costoInsumosFicha) throws Exception {
        String query = "execute sp_InsFichaProdDet "
                + fpd.getIdFichaProd()
                + ", " + fpd.getIdPartidaDet()
                + ", " + noPiezasPartida
                + ", " + fpd.getNoPiezasTotal()
                + ", " + kgPartida 
                + ", " + fpd.getKgTotal()
                + ", " + costoInsumosFicha;
        
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para eliminar una ficha de producción
    public static void eliminarFichaProd(FichaProdDet fpd) throws Exception {
        String query = "exec sp_eliFichaProd "
                + fpd.getIdFichaProdDet();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
