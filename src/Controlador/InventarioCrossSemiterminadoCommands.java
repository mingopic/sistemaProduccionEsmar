/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.InventarioCross;
import Modelo.Entity.InventarioCrossSemiterminado;
import Modelo.Entity.InventarioSemiterminado;
import Modelo.Entity.Partida;
import Modelo.Entity.TipoRecorte;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        String query = "exec sp_agrInvCrossSemi "
                + "@idPartida = "+ics.getIdPartida()+""
                + ", @fechaentrada = '"+ics.getFechaEntrada()+"'"
                + ", @idTipoRecorte = "+ics.getIdTipoRecorte()
                + ", @noPiezas = "+ics.getNoPiezas();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para obtener la lista de los cueros por trabajar
    public static List<InventarioCrossSemiterminado> obtenerListaInvCrossSemi(InventarioCrossSemiterminado ics, Partida p, TipoRecorte tr) throws Exception
    {
        List<InventarioCrossSemiterminado> lstInvCrossSemi = new ArrayList<>();
        String query;
        
        query= "EXEC sp_obtInvCrossSemi "
                + "'" + tr.getDescripcion() +"'"
                + "," + p.getNoPartida()
                + ",'" + ics.getFecha() +"'"
                + ",'" + ics.getFecha1() +"'";

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) 
        {
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                InventarioCrossSemiterminado obj = new InventarioCrossSemiterminado();
                obj.setIdPartida(rs.getInt("idPartida"));
                obj.setNoPartida(rs.getInt("noPartida"));
                obj.setIdTipoRecorte(rs.getInt("idTipoRecorte"));
                obj.setRecorte(rs.getString("recorte"));
                obj.setFechaEntrada(rs.getString("fechaentrada"));
                obj.setNoPiezas(rs.getInt("noPiezas"));
                obj.setNoPiezasActuales(rs.getInt("noPiezasActuales"));
                lstInvCrossSemi.add(obj);
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return lstInvCrossSemi;
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
