/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.ProcesoCommands.c;
import Modelo.Proceso;
import Modelo.SubProceso;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Equipo
 */
public class SubProcesoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    static SubProcesoCommands subP;
    
    //Método que se llama para obtener la lista de los cueros por trabajar
    public static String[][] obtenerListaSubprocesos(Proceso pr) throws Exception {
        String query;
        
        query= "EXEC sp_obtSubProc "+pr.getIdProceso()+";";

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 2;
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
                datos[i][0] = rs.getString("descripcion");
                datos[i][1] = rs.getString("idSubProceso");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método que se llama para obtener la lista de las formulas pertenecientes al subProceso señalado
    public static String[][] obtenerListaInsXSubProc(SubProceso subP) throws Exception {
        String query;
        
        query= "EXEC sp_obtFormInsXSubProc "+subP.getIdSubProceso()+";";

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 5;
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
                datos[i][0] = rs.getString("clave");
                datos[i][1] = rs.getString("porcentaje");
                datos[i][2] = rs.getString("idInsumo");
                datos[i][3] = rs.getString("idFormXSubProc");
                datos[i][4] = rs.getString("idInsumXProc");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
}
