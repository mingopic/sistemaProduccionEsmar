/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.ConfiguracionMerma;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Equipo
 */
public class ConfiguracionMermaCommands  {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método que se llama para obtener la configuracion de la merma de acuerdo a su fecha de configuracion mas reciente
    public static String[][] obtenerConfiguracionMerma(double sal, double humedad, double cachete, double tarimas, double kgTotales, double precio, int totalPiezas, int refMerma, String tipoCamion) throws Exception {
        String query;
        
        query= "EXEC sp_calCostTot "+sal+","+humedad+","+cachete+","+tarimas+","+kgTotales+","+precio+","+totalPiezas+","+refMerma+", '"+tipoCamion+"'";

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 24;
        int i = 0;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) 
        {
            renglones = rs.getRow();
            datos = new String[renglones][columnas];
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                datos[i][0] = rs.getString("totalPagar");
                datos[i][1] = rs.getString("salAcep");
                datos[i][2] = rs.getString("humedadAcepCalc");
                datos[i][3] = rs.getString("cacheteAcep");
                datos[i][4] = rs.getString("tarimasAcep");
                datos[i][5] = rs.getString("salReal");
                datos[i][6] = rs.getString("humedadReal");
                datos[i][7] = rs.getString("cacheteReal");
                datos[i][8] = rs.getString("tarimasReal");
                datos[i][9] = rs.getString("salDiferencia");
                datos[i][10] = rs.getString("humedadDiferencia");
                datos[i][11] = rs.getString("cacheteDiferencia");
                datos[i][12] = rs.getString("tarimasDiferencia");
                datos[i][13] = rs.getString("salDescontar");
                datos[i][14] = rs.getString("humedadDescontar");
                datos[i][15] = rs.getString("cacheteDescontar");
                datos[i][16] = rs.getString("tarimasDescontar");
                datos[i][17] = rs.getString("totalDescontar");
                datos[i][18] = rs.getString("humedadAcep");
                datos[i][19] = rs.getString("idConfigMermaSal");
                datos[i][20] = rs.getString("idConfigMermaHumedad");
                datos[i][21] = rs.getString("idConfigMermaCachete");
                datos[i][22] = rs.getString("idConfigMermaTarimas");
                datos[i][23] = rs.getString("kgTotalesConTarimas");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método que se llama para obtener la configuracion de la merma de acuerdo a su fecha de configuracion mas reciente
    public static String[][] obtenerConfiguracionesMerma() throws Exception {
        String query;
        
        query= "EXEC sp_obtConfMerma";

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 4;
        int i = 0;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) 
        {
            renglones = rs.getRow();
            datos = new String[renglones][columnas];
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                datos[i][0] = rs.getString("salAcep");
                datos[i][1] = rs.getString("humedadAcep");
                datos[i][2] = rs.getString("cacheteAcep");
                datos[i][3] = rs.getString("tarimasAcep");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para agregar una configuracion de merma a la tabla configMerma
    public static void agregarConfigMerma(ConfiguracionMerma[] datosCM) throws Exception {
        for (int i = 0; i < datosCM.length; i++) {
            String query = "exec sp_agrConfMerma "+datosCM[i].getIdTipoMerma()+""
                + ","+datosCM[i].getPorcMermaAcep();
            PreparedStatement pstmt = null;
            c.conectar();
            pstmt = c.getConexion().prepareStatement(query);
            System.out.println(query);
            pstmt.executeUpdate();
            c.desconectar();
        }
    }
}
