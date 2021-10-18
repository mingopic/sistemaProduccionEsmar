/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.BajasInventarioSemiterminado;
import Modelo.Entity.Calibre;
import Modelo.Entity.InventarioSemiterminado;
import Modelo.Entity.InventarioSemiterminadoTerminado;
import Modelo.Entity.Partida;
import Modelo.Entity.Seleccion;
import Modelo.Entity.TipoRecorte;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author EQUIPO-PC
 */
public class InventarioSemiterminadoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método que se llama para obtener la lista de los cueros por trabajar
    public static List<InventarioSemiterminado> obtenerListaInvSemiterminado(Partida p, TipoRecorte tr, Calibre ca, Seleccion s, InventarioSemiterminado is) throws Exception
    {
        List<InventarioSemiterminado> list = new ArrayList<>();
        String query;
        
        query = "EXEC sp_obtEntInvSem "
                + "@tipoRecorte = '" + tr.getDescripcion()+"'"
                + ", @calibre = " + "'"+ ca.getDescripcion() +"'"
                + ", @seleccion = " + "'"+ s.getDescripcion() +"'"
                + ", @noPartida = " + p.getNoPartida()
                + ", @fecha = " + "'"+ is.getFecha() +"'"
                + ", @fecha1 = " + "'"+ is.getFecha1() +"'";

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) 
        {
           rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                InventarioSemiterminado obj = new InventarioSemiterminado();
                obj.setIdPartida(rs.getInt("idPartida"));
                obj.setNoPartida(rs.getInt("noPartida"));
                obj.setIdTipoRecorte(rs.getInt("idTipoRecorte"));
                obj.setRecorte(rs.getString("recorte"));
                obj.setIdSeleccion(rs.getInt("idSeleccion"));
                obj.setSeleccion(rs.getString("seleccion"));
                obj.setIdCalibre(rs.getInt("idCalibre"));
                obj.setCalibre(rs.getString("calibre"));
                obj.setFechaEntrada(rs.getString("fechaEntrada"));
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //datos[i][7] = sdf.format(sqlDate);
                obj.setNoPiezas(rs.getInt("noPiezas"));
                obj.setNoPiezasActuales(rs.getInt("noPiezasActuales"));         
                obj.setKgTotales(Double.parseDouble((String.format("%.2f", rs.getDouble("kgTotales")))));
                obj.setKgTotalesActuales(Double.parseDouble((String.format("%.2f", rs.getDouble("kgTotalesActuales")))));
                obj.setPesoPromXPza(Double.parseDouble((String.format("%.2f", rs.getDouble("pesoPromXPza")))));
                
                list.add(obj);
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return list;
    }
    
    //Método para insertar el inventario completo de semiterminado
    public static void insInvSemiCompleto(String tipoRecorte, String calibre, String seleccion) throws Exception
    {
        String query = "execute sp_insInvSemiCompleto "
                + "'" + tipoRecorte + "'"
                + ", '" + calibre + "'"
                + ", '" + seleccion + "'";
        PreparedStatement pstmt = null;
         c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para agregar una entrada a la tabla tb_invSemiterminado
    public static void agregarInvSemiterminado(InventarioSemiterminado is) throws Exception
    {
        String query = "exec sp_agrInvSemi "
                +"@idPartida = "+ is.getIdPartida()
                + ", @fechaentrada = '" + is.getFechaEntrada()+"'"
                + ", @idTipoRecorte =" + is.getIdTipoRecorte()
                + ", @idCalibre = "+ is.getIdCalibre()
                + ", @idSeleccion = " + is.getIdSeleccion()
                + ", @noPiezas = " + is.getNoPiezas()
                + ", @kgTotales = " + is.getKgTotales();
        PreparedStatement pstmt = null;
        c.conectar();
        System.out.println(query);
        pstmt = c.getConexion().prepareStatement(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para actualizar el número de piezas actuales
    public static void actualizarNoPiezasActual(InventarioSemiterminadoTerminado ist, double kg) throws Exception
    {
        String query = "execute sp_actInvSemiterminado "+ist.getIdInvSemiterminado()+""
            + ","+ist.getNoPiezas()+","+kg;
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para actualizar el número de piezas actuales
    public static void actualizarNoPiezasBaja(BajasInventarioSemiterminado bis) throws Exception {
        String query = "exec sp_actBajasInvSemiterminado "
                + bis.getNoPiezas()
                + ", " + bis.getIdInvSemiterminado()
                + ", " + bis.getKgTotal();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para obtener la lista de entradas que se encuentran en terminado 
    public static List<InventarioSemiterminado> obtenerListaInvSemTer() throws Exception
    {
        List<InventarioSemiterminado> list = new ArrayList<>();
        String query;
        
        query = "execute Usp_InvSemTerGetAgrupado";

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) 
        {
           rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                InventarioSemiterminado obj = new InventarioSemiterminado();
                obj.setIdPartida(rs.getInt("idPartida"));
                obj.setNoPartida(rs.getInt("noPartida"));
                obj.setIdTipoRecorte(rs.getInt("idTipoRecorte"));
                obj.setRecorte(rs.getString("recorte"));
                obj.setIdSeleccion(rs.getInt("idSeleccion"));
                obj.setSeleccion(rs.getString("seleccion"));
                obj.setIdCalibre(rs.getInt("idCalibre"));
                obj.setCalibre(rs.getString("calibre"));
                obj.setFechaEntrada(rs.getString("fechaEntrada"));
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //datos[i][7] = sdf.format(sqlDate);
                obj.setNoPiezasActuales(rs.getInt("noPiezasActuales"));         

                list.add(obj);
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return list;
    }
    
    //Método para agregar una entrada a la tabla tb_invSemiterminado
    public static void regresarPartidaSemiterminado(InventarioSemiterminado is) throws Exception
    {
        String query = "exec Usp_InvSemiterminadoregresar "
                +"@idPartida = "+ is.getIdPartida()
                + ", @idTipoRecorte =" + is.getIdTipoRecorte()
                + ", @idCalibre = "+ is.getIdCalibre()
                + ", @idSeleccion = " + is.getIdSeleccion()
                + ", @fechaentrada = '" + is.getFechaEntrada()+"'"
                + ", @noPiezas = " + is.getNoPiezas();
        PreparedStatement pstmt = null;
        c.conectar();
        System.out.println(query);
        pstmt = c.getConexion().prepareStatement(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
