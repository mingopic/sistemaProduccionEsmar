/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Dto.EntradaInvCross;
import Modelo.Entity.BajasInventarioCross;
import Modelo.Entity.InventarioCross;
import Modelo.Entity.InventarioCrossSemiterminado;
import Modelo.Entity.Partida;
import Modelo.Entity.PartidaDetalle;
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
public class InventarioCrossCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método que se llama para obtener la lista del inventario de cross
    public static List<EntradaInvCross> obtenerListaInvCross(InventarioCross ic, TipoRecorte tr, Partida p) throws Exception
    {
        List<EntradaInvCross> lstEntradas = new ArrayList<>();
        String query;
        
        query= "execute sp_obtEntCross "
                + "@tipoRecorte = '" + tr.getDescripcion() +"'"
                + ", @noPartida = " + p.getNoPartida()
                + ", @fecha = '" + ic.getFecha() + "'"
                + ", @fecha1 = '" + ic.getFecha1() +"'";

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) 
        {
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                EntradaInvCross obj = new EntradaInvCross();
                obj.setIdPartida(rs.getInt("idPartida"));
                obj.setFechaEntrada(rs.getString("fechaentrada"));
                obj.setNoPartida(rs.getInt("noPartida"));
                obj.setIdTipoRecorte(rs.getInt("idTipoRecorte"));
                obj.setRecorte(rs.getString("descripcion"));
                obj.setNoPiezas(rs.getInt("noPiezas"));
                obj.setNoPiezasActuales(rs.getInt("noPiezasActuales"));
                obj.setKgTotal(rs.getDouble("kgTotal"));
                obj.setKgActual(rs.getDouble("kgActual"));
                lstEntradas.add(obj);
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return lstEntradas;
    }
    
    //Método para actualizar el número de piezas actuales
    public static void actualizarNoPiezasActual(InventarioCrossSemiterminado ics) throws Exception
    {
        String query = "exec sp_actInvCross "+ics.getIdInvPCross()+""
            + ","+ics.getNoPiezas()+","+ics.getKgTotal();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para insertar en la tabla de tb_invCross
    public static void insertarInvCross(PartidaDetalle pd) throws Exception
    {
        String query = "execute sp_insInvCross "
                + pd.getIdPartida()
                + ", " + pd.getIdTipoRecorte()
                + ", " + pd.getNoPiezas();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para actualizar el número de piezas actuales
    public static void actualizarNoPiezasBaja(BajasInventarioCross bic) throws Exception {
        String query = "exec sp_actBajasInvCross "
                + bic.getNoPiezas()
                + ", " + bic.getIdInvPCross()
                + ", " + bic.getKgTotal();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
