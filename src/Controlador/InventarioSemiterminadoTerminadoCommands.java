/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Calibre;
import Modelo.InventarioSemiterminadoTerminado;
import Modelo.InventarioTerminado;
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
    
    //Método que se llama para obtener la lista del Inventario Semiterminado Terminado Completo
    public static String[][] obtenerListaInvSemTer() throws Exception
    {
        String query;
        
        // Este SP es el que modificas
        query= "execute sp_obtInvSemTer ";

        String[][] datos = null;
        int renglones = 0;
        int columnas = 9;
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
                datos[i][1] = rs.getString("tipoRecorte");
                datos[i][2] = rs.getString("calibre");
                datos[i][3] = rs.getString("seleccion");
                datos[i][4] = rs.getString("noPiezas");
                datos[i][5] = String.format("%.2f",Double.parseDouble(rs.getString("kgTotales")));
                Date sqlDate = rs.getDate("fechaEntrada");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                datos[i][6] = sdf.format(sqlDate);
                
                datos[i][7] = rs.getString("bandera");
                datos[i][8] = rs.getString("idInvSemTer");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para actualizar el número de piezas actuales
    public static void actualizarNoPiezasActual(InventarioTerminado it) throws Exception
    {
        String query = "exec sp_actInvSemTer "
                + it.getIdInvSemTer()
                + "," + it.getNoPiezas();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para insertar el inventario completo de semiterminado terminado
    public static void insInvSemTerCompleto() throws Exception
    {
        String query = "execute sp_insInvSemTerCompleto ";
        PreparedStatement pstmt = null;
         c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
