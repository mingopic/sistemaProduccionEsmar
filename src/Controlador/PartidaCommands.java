/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Partida;
import Modelo.PartidaDisp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author EQUIPO-PC
 */
public class PartidaCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método que se llama para obtener el número de partidas registradas
    public static String[] llenarNoPartidas() throws Exception {
        String query;
        
        query= "EXEC sp_obtNoPartida";

        
        String[] datos = null;
        int renglones = 0;
        int i = 0;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) 
        {
            renglones = rs.getRow();
            datos = new String[renglones];
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                datos[i] = rs.getString("noPartida");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para agregar una partida
    public static void agregarPartida(Partida p) throws Exception {
        String query = "exec sp_agrPartida "+p.getNoPartida()+","+p.getNoTotalPiezas()+","+p.getIdProceso();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
     //Método que se llama para obtener el número de partidas registradas
    public static List<PartidaDisp> obtenerPartidasDisponibles(Partida p) 
    {
        List<PartidaDisp> partidas = null;
        String query = "execute sp_obtPartidaXproceso "+p.getIdProceso();

        Statement stmt = null;
        ResultSet rs = null;
        
        try 
        {
            c.conectar();
            stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(query);
            rs = stmt.executeQuery(query);

            partidas = new ArrayList<>();
            if (rs.last()) 
            {
                rs.beforeFirst();

                //Recorremos el ResultSet registro a registro
                while (rs.next()) {
                    PartidaDisp pd = new PartidaDisp();
                    pd.setNoPartida(rs.getInt("NoPartida"));
                    pd.setTipoRecorte(rs.getString("descripcion"));
                    pd.setNoPiezasAct(rs.getInt("noPiezasAct"));
                    pd.setIdPartidaDet(rs.getInt("idPartidaDet"));
                    pd.setIdPartida(rs.getInt("idPartida"));
                    pd.setIdTipoRecorte(rs.getInt("idTipoRecorte"));
                    partidas.add(pd);
                }
            }
            rs.close();
            stmt.close();
            c.desconectar();
        } 
        catch (Exception e) 
        {
            System.err.println(e);
        }
        return partidas;
    }
}
