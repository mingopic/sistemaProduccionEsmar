/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Calibre;
import Modelo.InventarioCross;
import Modelo.InventarioSemiterminado;
import Modelo.Seleccion;
import Modelo.TipoCuero;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author EQUIPO-PC
 */
public class InventarioSemiterminadoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método que se llama para obtener la lista de los cueros por trabajar
    public static String[][] obtenerListaInvSemiterminado(InventarioCross ic, TipoCuero tc, Calibre ca, Seleccion s, InventarioSemiterminado is) throws Exception
    {
        String query;
        
        query= "EXEC sp_obtEntInvSem '"+tc.getDescripcion()+"','"+ca.getDescripcion()+"','"+s.getDescripcion()+"',"
                +ic.getIdPartida()+",'"+is.getFecha()+"','"+is.getFecha1()+"'";

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 10;
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
                datos[i][0] = rs.getString("idPartida");
                datos[i][1] = rs.getString("tipoCuero");
                datos[i][2] = rs.getString("noPiezas");
                datos[i][3] = rs.getString("noPiezasActuales");
                datos[i][4] = rs.getString("kgTotales");
                datos[i][5] = rs.getString("PesoPromXPza");
                datos[i][6] = rs.getString("seleccion");
                datos[i][7] = rs.getString("calibre");
                
                Date sqlDate = rs.getDate("fechaEntrada");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                datos[i][8] = sdf.format(sqlDate);
                datos[i][9] = rs.getString("idInvSemiterminado");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
}
