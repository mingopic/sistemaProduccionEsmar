/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Equipo
 */
public class ConfiguracionMermaCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método que se llama para obtener la configuracion de la merma de acuerdo a su fecha de configuracion mas reciente
    public static String[][] obtenerConfiguracionMerma(double sal, double humedad, double cachete, double tarimas, double kgTotales, double precio, int totalPiezas, int refMerma) throws Exception {
        String query;
        
        query= "EXEC sp_calCostTot "+sal+","+humedad+","+cachete+","+tarimas+","+kgTotales+","+precio+","+totalPiezas+","+refMerma;

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 1;
        int i = 0;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) 
        {
            renglones = rs.getRow();
            datos = new String[renglones][columnas];
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                datos[i][0] = rs.getString("totalPagar");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
}
