/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.FichaProduccion;
import Modelo.Proceso;
import Modelo.TipoRecorte;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author EQUIPO-PC
 */
public class FichaProduccionCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método que se llama para obtener la lista de las entradas de producción en proceso
    public static String[][] obtenerListaProduccionProceso(FichaProduccion fp, Proceso pr, TipoRecorte tr) throws Exception
    {
        String query;
        
        query= "EXEC sp_obtEntProdProc "
                + "'" + pr.getDescripcion()+"'"
                + ",'" + tr.getDescripcion()+"'"
                + "," + "'"+ fp.getFecha() +"'"
                + "," + "'"+ fp.getFecha1() +"'";

        String[][] datos = null;
        int renglones = 0;
        int columnas = 13;
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
                datos[i][0] = rs.getString("idFichaProd");
                datos[i][1] = rs.getString("noPartida");
                datos[i][2] = rs.getString("proceso");
                datos[i][3] = rs.getString("tipoRecorte");
                datos[i][4] = rs.getString("noPiezasTotal");
                datos[i][5] = rs.getString("kgTotal");
                datos[i][6] = String.format("%.2f",Double.parseDouble(rs.getString("costoTotalCuero")));
                datos[i][7] = String.format("%.2f",Double.parseDouble(rs.getString("costoCueroXpza")));
                datos[i][8] = String.format("%.2f",Double.parseDouble(rs.getString("costoInsumos")));
                datos[i][9] = String.format("%.2f",Double.parseDouble(rs.getString("costoInsumoxKg")));
                datos[i][10] = rs.getString("tambor");
                
                Date sqlDate = rs.getDate("fechaCreacion");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                datos[i][11] = sdf.format(sqlDate);
                datos[i][12] = rs.getString("idFichaProdDet");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
}
