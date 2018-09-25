/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.InventarioCrudo;
import Modelo.RecepcionCuero;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author EQUIPO-PC
 */
public class InventarioCrudoCommands {
    static Statement stmt = null;
    static PreparedStatement pstmt;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método que se llama para obtener la lista de las recepciones de cuero con piezas disponibles
    public static String[][] obtenerListaInvCueroCrudo() throws Exception {
        String query= "EXEC sp_obtRecCueroDisp";
        
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
                datos[i][1] = rs.getString("noCamion");
                datos[i][2] = rs.getString("descripcion");
                datos[i][3] = rs.getString("recorte");
                datos[i][4] = rs.getString("noPiezasActual");
                datos[i][5] = (String.format("%.2f",Double.parseDouble(rs.getString("kgTotalActual"))));
                datos[i][6] = (String.format("%.2f",Double.parseDouble(rs.getString("PromKgPieza"))));
                datos[i][7] = rs.getString("fechaEntrada");
                datos[i][8] = rs.getString("idRecepcionCuero");
                datos[i][9] = rs.getString("idInventarioCrudo");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para actualizar el número de piezas actuales
    public static void actualizarNoPiezasActual(String[][] datosPar) throws Exception {
        for (int i = 0; i < datosPar.length; i++) {
            String query = "exec sp_actInvCrudo "
                    + datosPar[i][3]
                    + ", " + datosPar[i][4]
                    + ", " + datosPar[i][5];
            PreparedStatement pstmt = null;
            c.conectar();
            pstmt = c.getConexion().prepareStatement(query);
            System.out.println(query);
            pstmt.executeUpdate();
            c.desconectar();
        }
    }
    
    //Método para eliminar una recepcion de cuero que aún no está en uso
    public static void eliminarInventarioCrudo(RecepcionCuero rc) throws Exception {
        String query = "exec sp_EliInvCrudo "+rc.getIdRecepcionCuero();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para agregar recortes de cuero en inventarioCrudo
    public static void agregarRecorte(InventarioCrudo ic, int noPiezasOriginal, int noPiezas1, int noPiezas2, Double kg1, Double kg2 ) throws Exception {
        String query = "exec sp_insRecorteInvCrudo "
                + ic.getIdInventarioCrudo()
                + ", " + noPiezasOriginal
                + ", " + noPiezas1
                + ", " + noPiezas2
                + ", " + kg1
                + ", " + kg2
                + ", " + ic.getIdTipoRecorte()
                + ", " + ic.getIdRecepcionCuero();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
