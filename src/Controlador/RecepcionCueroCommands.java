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
    static ConexionBD c = new ConexionBD();
     
    //Método que se llama para obtener la lista de los cueros por trabajar
    public static String[][] obtenerListaRecepcionCuero(RecepcionCuero rc, TipoCuero tp) throws Exception {
        String query;
        
        query= "EXEC sp_obtEntReccuero '"+rc.getProveedor()+"','"+tp.getDescripcion()+"','"+rc.getFecha()+"','"+rc.getFecha1()+"';";

        
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
                datos[i][0] = rs.getString("nombreProveedor");
                datos[i][1] = rs.getString("descripcion");
                datos[i][2] = rs.getString("noCamion");
                datos[i][3] = rs.getString("noTotalPiezas");
                datos[i][4] = rs.getString("kgTotal");
                datos[i][5] = rs.getString("precioXKilo");
                datos[i][6] = rs.getString("costoCamion");
                datos[i][7] = rs.getString("origen");
                
                Date sqlDate = rs.getDate("fechaEntrada");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                datos[i][8] = sdf.format(sqlDate);
                datos[i][9] = rs.getString("idRecepcionCuero");
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
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
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
                +rc.getMermaHumedad()+","+rc.getMermaCachete()+","+rc.getMermaTarimas()+","+rc.getRefParaMerma()+","
                +rc.getIdMerSal()+","+rc.getIdMerHum()+","+rc.getIdMerCac()+","+rc.getIdMerTar()+", '"+rc.getTipoCamion()+"'";

        
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para obtener el detalle de ña recepcion de cuero seleccionada
    public static String[][] obtenerListaRecepcionCueroDetalle(RecepcionCuero rc, TipoCuero tp) throws Exception {
        String query;
        
        query= "EXEC sp_obtEntReccueroDetalle '"+rc.getIdRecepcionCuero();

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 35;
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
                
                Date sqlDate = rs.getDate("fechaEntrada");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                datos[i][1] = sdf.format(sqlDate);
                
                datos[i][2] = rs.getString("noCamion");
                datos[i][3] = rs.getString("kgTotal");
                datos[i][4] = rs.getString("precioXKilo");
                datos[i][5] = rs.getString("costoCamion");
                datos[i][6] = rs.getString("rangoMin");
                datos[i][7] = rs.getString("rangoMax");
                datos[i][8] = rs.getString("noPiezasLigero");
                datos[i][9] = rs.getString("noPiezasPesado");
                datos[i][10] = rs.getString("noTotalPiezas");
                datos[i][11] = rs.getString("mermaSal");
                datos[i][12] = rs.getString("mermaHumedad");
                datos[i][13] = rs.getString("mermaCachete");
                datos[i][14] = rs.getString("mermaTarimas");
                datos[i][15] = rs.getString("salAcep");
                datos[i][16] = rs.getString("humedadAcep");
                datos[i][17] = rs.getString("cacheteAcep");
                datos[i][18] = rs.getString("tarimasAcep");
                datos[i][19] = rs.getString("salReal");
                datos[i][20] = rs.getString("humedadReal");
                datos[i][21] = rs.getString("cacheteReal");
                datos[i][22] = rs.getString("tarimasReal");
                datos[i][23] = rs.getString("salDif");
                datos[i][24] = rs.getString("humedadDif");
                datos[i][25] = rs.getString("cacheteDif");
                datos[i][26] = rs.getString("tarimasDif");
                datos[i][27] = rs.getString("salDesc");
                datos[i][28] = rs.getString("humedadDesc");
                datos[i][29] = rs.getString("cacheteDesc");
                datos[i][30] = rs.getString("tarimasDesc");
                datos[i][31] = rs.getString("totalDescKg");
                datos[i][32] = rs.getString("pesoCamion");
                datos[i][33] = rs.getString("totalPagarKg");
                datos[i][34] = rs.getString("totalPagar");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para eliminar una recepcion de cuero que aún no está en uso
    public static void eliminarRecepcionCuero(RecepcionCuero rc) throws Exception {
        String query = "exec sp_EliRecCuero "+rc.getIdRecepcionCuero();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
