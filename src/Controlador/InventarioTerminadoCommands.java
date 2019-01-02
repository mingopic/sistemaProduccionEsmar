/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.BajasInventarioTerminado;
import Modelo.Calibre;
import Modelo.InventarioSalTerminado;
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
public class InventarioTerminadoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método que se llama para obtener la lista de las entradas de inventario terminado
    public static String[][] obtenerListaInvTerminado(Partida p, TipoRecorte tr, Calibre ca, Seleccion s, InventarioTerminado it) throws Exception
    {
        String query;
        
        query= "EXEC sp_obtEntInvTer "
                + "'" + tr.getDescripcion()+"'"
                + "," + "'"+ ca.getDescripcion() +"'"
                + "," + "'"+ s.getDescripcion() +"'"
                + "," + p.getNoPartida()
                + "," + "'"+ it.getFecha() +"'"
                + "," + "'"+ it.getFecha1() +"'";

        String[][] datos = null;
        int renglones = 0;
        int columnas = 15;
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
                datos[i][2] = rs.getString("noPiezasActuales");
                
                try 
                {
                    datos[i][3] = String.format("%.2f",Double.parseDouble(rs.getString("kgTotalesActual")));
                } 
                catch (Exception e) 
                {
                    datos[i][3] = String.valueOf(0.0);
                }
                
                try 
                {
                    datos[i][4] = String.format("%.2f",Double.parseDouble(rs.getString("PesoPromXPza")));
                } 
                catch (Exception e) 
                {
                    
                }
                
                if (datos[i][4] == null)
                {
                    datos[i][4] = String.valueOf(0.0);
                }
                
                datos[i][5] = String.format("%.2f",Double.parseDouble(rs.getString("decimetrosActual")));
                if (datos[i][5] == null)
                {
                    datos[i][5] = String.valueOf(0.0);
                }
                
                datos[i][6] = String.format("%.2f",Double.parseDouble(rs.getString("piesActual")));
                if (datos[i][6] == null)
                {
                    datos[i][6] = String.valueOf(0.0);
                }
                
                datos[i][7] = rs.getString("seleccion");
                datos[i][8] = rs.getString("calibre");
                Date sqlDate = rs.getDate("fechaEntrada");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                datos[i][9] = sdf.format(sqlDate);
                
//                datos[i][10] = rs.getString("noPiezas");
//                datos[i][11] = String.format("%.2f",Double.parseDouble(rs.getString("kgTotales")));
//                datos[i][12] = String.format("%.2f",Double.parseDouble(rs.getString("decimetros")));
//                datos[i][13] = String.format("%.2f",Double.parseDouble(rs.getString("pies")));
                datos[i][10] = rs.getString("idInvTerminado");
                datos[i][11] = rs.getString("bandera");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
     //Método para agregar una entrada a la tabla tb_invTerminado
    public static void agregarInvTerminado(InventarioTerminado it) throws Exception
    {
        String query = "execute sp_agrInvTer "
                + it.getIdInvSemTer()
                + "," + it.getBandera()
                + "," + it.getIdCalibre()
                + ","+ it.getIdSeleccion()
                + ","+ it.getNoPiezas()
                + "," + it.getKgTotales()
                + "," + it.getDecimetros()
                + "," + it.getPies();
        PreparedStatement pstmt = null;
        c.conectar();
        System.out.println(query);
        pstmt = c.getConexion().prepareStatement(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para actualizar el número de piezas actuales
    public static void actualizarNoPiezasActual(InventarioSalTerminado isalt) throws Exception
    {
        String query = "exec sp_actInvTer "
                + isalt.getIdInvTerminado()
                + "," + isalt.getNoPiezas()
                + "," + isalt.getKg()
                + "," + isalt.getDecimetros()
                + "," + isalt.getPies()
                + "," + isalt.getBandera();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para actualizar el número de piezas actuales
    public static void actualizarNoPiezasBaja(BajasInventarioTerminado bit) throws Exception {
        String query = "exec sp_actBajasInvTerminado "
                + bit.getNoPiezas()
                + ", " + bit.getIdInvTerminado()
                + ", " + bit.getKg()
                + ", " + bit.getPies()
                + ", " + bit.getDecimetros()
                + ", " + bit.getBandera();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para insertar el inventario completo de terminado
    public static void insInvTerminadoCompleto() throws Exception
    {
        String query = "execute sp_insInvTerminadoCompleto ";
        PreparedStatement pstmt = null;
         c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
