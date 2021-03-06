/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.RecepcionCuero;
import Modelo.TipoCuero;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Equipo
 */
public class RecepcionCueroCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método que se llama para obtener la lista de los cueros por trabajar
    public static String[][] obtenerListaRecepcionCuero(RecepcionCuero rc, TipoCuero tp) throws Exception {
        String query;
        
        query= "EXEC sp_obtEntReccuero '"+rc.getProveedor()+"','"+tp.getDescripcion()+"','"+rc.getFecha()+"','"+rc.getFecha1()+"';";

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 8;
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
                datos[i][0] = rs.getString("nombreProveedor");
                datos[i][1] = rs.getString("descripcion");
                datos[i][2] = rs.getString("noCamion");
                datos[i][3] = rs.getString("noTotalPiezas");
                datos[i][4] = rs.getString("kgTotal");
                datos[i][5] = rs.getString("precioXKilo");
                datos[i][6] = rs.getString("costoCamion");
                
                Date sqlDate = rs.getDate("fechaEntrada");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                datos[i][7] = sdf.format(sqlDate);
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método que se llama para obtener la lista de los cueros por trabajar
    public static String[] llenarNoCamiones(RecepcionCuero rc) throws Exception {
        String query;
        
        query= "EXEC sp_obtNoCamion "+rc.getIdProveedor();

        
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
                datos[i] = rs.getString("noCamion");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método que se llama para insertar una entrada de recepcion de cuero
    public static void InsertarEntradaRecepcionCuero(RecepcionCuero rc) throws Exception {
        String query;
        
        query= "EXEC sp_agrEntRecCuero "+rc.getIdProveedor()+","+rc.getNoCamion()+","+rc.getIdTipoCuero()+","
                +rc.getIdRangoPesoCuero()+","+rc.getNoPiezasLigero()+","+rc.getNoPiezasPesado()+","
                +rc.getNoTotalPiezas()+","+rc.getKgTotal()+","+rc.getPrecioXKilo()+","+rc.getMermaSal()+","
                +rc.getMermaHumedad()+","+rc.getMermaCachete()+","+rc.getMermaTarimas();

        
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
