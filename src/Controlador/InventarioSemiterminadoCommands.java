/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Calibre;
import Modelo.InventarioCross;
import Modelo.InventarioSemiterminado;
import Modelo.InventarioSemiterminadoTerminado;
import Modelo.Partida;
import Modelo.Seleccion;
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
public class InventarioSemiterminadoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método que se llama para obtener la lista de los cueros por trabajar
    public static String[][] obtenerListaInvSemiterminado(Partida p, TipoRecorte tr, Calibre ca, Seleccion s, InventarioSemiterminado is) throws Exception
    {
        String query;
        
        query= "EXEC sp_obtEntInvSem "
                + "'" + tr.getDescripcion()+"'"
                + "," + "'"+ ca.getDescripcion() +"'"
                + "," + "'"+ s.getDescripcion() +"'"
                + "," + p.getNoPartida()
                + "," + "'"+ is.getFecha() +"'"
                + "," + "'"+ is.getFecha1() +"'";

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
                datos[i][0] = rs.getString("noPartida");
                datos[i][1] = rs.getString("tipoRecorte");
                datos[i][2] = rs.getString("noPiezas");
                datos[i][3] = rs.getString("noPiezasActuales");
                datos[i][4] = rs.getString("kgTotales");
                datos[i][5] = String.format("%.2f",Double.parseDouble(rs.getString("PesoPromXPza")));
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
    
    //Método para agregar una entrada a la tabla tb_invSemiterminado
    public static void agregarInvSemiterminado(InventarioSemiterminado is) throws Exception
    {
        String query = "exec sp_agrInvSemi "
                + is.getIdInvCrossSemi()
                + "," + is.getIdCalibre()
                + ","+ is.getIdSeleccion()
                + ","+ is.getNoPiezas()
                + "," + is.getKgTotales();
        PreparedStatement pstmt = null;
        c.conectar();
        System.out.println(query);
        pstmt = c.getConexion().prepareStatement(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para actualizar el número de piezas actuales
    public static void actualizarNoPiezasActual(InventarioSemiterminadoTerminado ist) throws Exception
    {
        String query = "execute sp_actInvSemiterminado "+ist.getIdInvSemiterminado()+""
            + ","+ist.getNoPiezas();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
