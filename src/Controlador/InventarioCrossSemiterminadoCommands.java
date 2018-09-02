/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.InventarioCross;
import Modelo.InventarioCrossSemiterminado;
import Modelo.InventarioSemiterminado;
import Modelo.Partida;
import Modelo.TipoRecorte;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author EQUIPO-PC
 */
public class InventarioCrossSemiterminadoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para agregar una entrada a la tabla tb_invCrossSemi
    public static void agregarInvCrossSemi(InventarioCrossSemiterminado ics) throws Exception
    {
        String query = "exec sp_agrInvCrossSemi "+ics.getIdInvPCross()+""
            + ","+ics.getNoPiezas()+","+ics.getNoPiezasActuales()+","+ics.getKgTotal();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para obtener la lista de los cueros por trabajar
    public static String[][] obtenerListaInvCrossSemi(InventarioCrossSemiterminado ics, Partida p, TipoRecorte tr) throws Exception
    {
        String query;
        
        query= "EXEC sp_obtInvCrossSemi "
                + "'" + tr.getDescripcion() +"'"
                + "," + p.getNoPartida()
                + ",'" + ics.getFecha() +"'"
                + ",'" + ics.getFecha1() +"'";

        String[][] datos = null;
        int renglones = 0;
        int columnas = 6;
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
                datos[i][0] = rs.getString("noPartida");
                datos[i][1] = rs.getString("descripcion");
                datos[i][2] = rs.getString("noPiezas");
                datos[i][3] = rs.getString("noPiezasActuales");
                
                Date sqlDate = rs.getDate("fechaEntrada");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                datos[i][4] = sdf.format(sqlDate);
                datos[i][5] = rs.getString("idInvCrossSemi");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para actualizar el número de piezas actuales
    public static void actualizarNoPiezasActual(InventarioSemiterminado is) throws Exception
    {
        String query = "exec sp_actInvCrossSemi "
                + is.getIdInvCrossSemi()
                + "," + is.getNoPiezas();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
