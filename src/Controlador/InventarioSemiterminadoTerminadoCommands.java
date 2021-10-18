/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.InventarioSemiterminadoTerminado;
import Modelo.Entity.InventarioTerminado;
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
public class InventarioSemiterminadoTerminadoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para agregar una entrada a la tabla tb_invSemTer
    public static void agregarInvSemTer(InventarioSemiterminadoTerminado ist) throws Exception
    {
        String query = "execute sp_agrInvSemTer "
                + "@noPiezas = " + ist.getNoPiezas()
                + ", @kgTotales = "+ist.getKgTotales()
                + ", @idPartida = "+ist.getIdPartida()
                + ", @idTipoRecorte = "+ist.getIdTipoRecorte()
                + ", @idCalibre = "+ist.getIdCalibre()
                + ", @idSeleccion = "+ist.getIdSeleccion()
                + ", @fechaentrada = '"+ist.getFechaEntrada()+"'";
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para obtener la lista del Inventario Semiterminado Terminado Completo
    public static List<InventarioSemiterminadoTerminado> obtenerListaInvSemTer() throws Exception
    {
        List<InventarioSemiterminadoTerminado> list = new ArrayList<>();
        String query;
        
        // Este SP es el que modificas
        query= "execute sp_obtInvSemTer ";

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) 
        {
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                InventarioSemiterminadoTerminado obj = new InventarioSemiterminadoTerminado();
                obj.setNoPartida(rs.getInt("noPartida"));
                obj.setIdTipoRecorte(rs.getInt("idTipoRecorte"));
                obj.setTipoRecorte(rs.getString("tipoRecorte"));
                obj.setIdCalibre(rs.getInt("idCalibre"));
                obj.setCalibre(rs.getString("calibre"));
                obj.setIdSeleccion(rs.getInt("idSeleccion"));
                obj.setSeleccion(rs.getString("seleccion"));
                obj.setFechaEntrada(rs.getString("fechaEntrada"));
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //datos[i][7] = sdf.format(sqlDate);
                obj.setBandera(rs.getInt("bandera"));
                obj.setNoPiezas(rs.getInt("noPiezas"));    
                obj.setKgTotales(Double.parseDouble((String.format("%.2f", rs.getDouble("kgTotales")))));
                
                list.add(obj);
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return list;
    }
    
    //Método para actualizar el número de piezas actuales
    public static void actualizarNoPiezasActual(InventarioTerminado it) throws Exception
    {
        String query = "exec sp_actInvSemTer "
                + it.getIdInvSemTer()
                + "," + it.getNoPiezas()
                + "," + it.getBandera();
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
