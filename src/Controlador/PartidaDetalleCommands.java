/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.PartidaDetalle;
import Modelo.PartidaDisp;
import Modelo.RecepcionCuero;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author EQUIPO-PC
 */
public class PartidaDetalleCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método para agregar una entrada a la tabla entradaProductoAlmacen
    public static void agregarPartidaDetalle(PartidaDetalle[] datosPD, String[][] datosPar) throws Exception {
        for (int i = 0; i < datosPD.length; i++) {
            String query = "exec sp_agrPartidaDetalle "+datosPD[i].getNoPiezas()+""
                + ","+datosPD[i].getIdPartida()+","+datosPD[i].getIdTipoRecorte()+""
                    + ",'"+datosPar[i][0]+"',"+datosPar[i][1]+",'"+datosPar[i][2]+"'";
            PreparedStatement pstmt = null;
            c.conectar();
            pstmt = c.getConexion().prepareStatement(query);
            System.out.println(query);
            pstmt.executeUpdate();
            c.desconectar();
        }
    }
    
    //Método para agregar una entrada a la tabla entradaProductoAlmacen
    public void agregarRecorte(PartidaDisp datosPartida, int noPiezasAct, int noPiezas, int idProceso)
    {
        try 
        {
            String query = "exec sp_insRecorte "
                    + datosPartida.getIdPartidaDet()
                    + "," + datosPartida.getIdTipoRecorte()
                    + "," + noPiezasAct
                    + "," + noPiezas
                    + "," + datosPartida.getIdPartida()
                    + "," + idProceso;
            
            PreparedStatement pstmt = null;
            
            c.conectar();
            pstmt = c.getConexion().prepareStatement(query);
            System.out.println(query);
            pstmt.executeUpdate();
            c.desconectar();
        } 
        catch (Exception e) 
        {
            System.err.println(e);
        }
    }
    
    //Método que se llama para obtener la idRecepcionCuero a eliminar
    public static int obtenerRecepcionCueroEliminar(RecepcionCuero rc) throws Exception {
        String query= "execute sp_obtRecCueroEli "+rc.getIdRecepcionCuero();
        
        int datos = 0;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) 
        {
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                datos = rs.getInt("idRecepcionCuero");
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para agregar una entrada a la tabla entradaProductoAlmacen
    public static void insPartidaDetFicha(PartidaDetalle pd) throws Exception {
        String query = "exec sp_InsPartidaDetalleFicha "
                + pd.getNoPiezas()
                + ", " + pd.getIdPartida()
                + ", " + pd.getIdPartidaDet()
                + ", " + pd.getIdTipoRecorte();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para obtener la idRecepcionCuero a eliminar
    public static int obtenerUltPartidaDet() throws Exception {
        String query= "execute sp_ObtUltPartidaDet ";
        
        int id = 0;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) 
        {
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                id = rs.getInt("idPartidaDet");
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return id;
    }
}
