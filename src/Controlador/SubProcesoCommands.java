/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.ProcesoCommands.c;
import Modelo.Proceso;
import Modelo.Proveedor;
import Modelo.SubProceso;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Equipo
 */
public class SubProcesoCommands {
    static Statement stmt = null;
    static PreparedStatement pstmt;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    static SubProcesoCommands subP;
    
    //Método que se llama para obtener la lista de los cueros por trabajar
    public static String[][] obtenerListaSubprocesosXid(Proceso pr) throws Exception {
        String query= "execute sp_obtSubProcXid "+pr.getIdProceso()+";";
        
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
    
    //Método para obtener todos los subprocesos registrados de BD
    public static String[][] obtenerSubprocesos(String proceso) throws Exception {
        String query= "EXEC sp_obtSubProc '"+proceso+"'";

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
                datos[i][0] = rs.getString("subProceso");
                datos[i][1] = rs.getString("proceso");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para buscar datos de un subProceso en BD
    public static SubProceso obtSubProcXdesc(String subProceso) throws Exception
    {   
        SubProceso sp = new SubProceso();
        String query="execute sp_obtSubProcXdesc '"+subProceso+"'";

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.next())
        {
            sp.setIdSubProceso(Integer.parseInt(rs.getString("idSubProceso")));
            sp.setDescripcion(rs.getString("descripcion"));
            sp.setIdProceso(Integer.parseInt(rs.getString("idProceso")));
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return sp;
    }
    
    //Método que se llama para insertar un subProceso
    public static void insertarSubProceso(SubProceso s, String proceso) throws Exception {
        String query= "execute sp_insSubProc '"+s.getDescripcion()+"', '"+proceso+"'";
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para actualizar detos de un subProceso
    public static void actualizarSubProceso(SubProceso s, String proceso) throws Exception {
        String query= "execute sp_actSubProc '"+s.getDescripcion()+"', '"+proceso+"', "+s.getIdSubProceso();
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
