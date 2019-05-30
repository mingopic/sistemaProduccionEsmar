/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.BajasPartidaDet;
import Modelo.FichaProdDet;
import Modelo.FichaProduccion;
import Modelo.PartidaDetalle;
import Modelo.PartidaDisp;
import Modelo.RecepcionCuero;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author EQUIPO-PC
 */
public class PartidaDetalleCommands {
    static Statement stmt = null;
    static PreparedStatement pstmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método para agregar una entrada a la tabla entradaProductoAlmacen
    public static void agregarPartidaDetalle(PartidaDetalle[] datosPD, String[][] datosPar) throws Exception {
        for (int i = 0; i < datosPD.length; i++) {
            String query = "exec sp_agrPartidaDetalle "
                    + datosPD[i].getNoPiezas()
                    + ", " + datosPD[i].getIdPartida()
                    + ", '" + datosPD[i].getRecorte() + "' "
                    + ", '" + datosPar[i][0] + "' "
                    + ", " + datosPar[i][1] 
                    + ", '" + datosPar[i][2] + "'"
                    + ", " + datosPar[i][5];
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
    
    //Método que se llama para validar la partidaDet a eliminar
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
    
    //Método que se llama para obtener la idPartidaDet a eliminar
    public static int obtenerPartidaDetalleEliminar(PartidaDetalle pd) throws Exception {
        String query= "execute sp_obtPartDetEli "+pd.getIdPartidaDet()+","+pd.getIdPartida();
        
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
                datos = rs.getInt("eliminar");
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método que se llama eliminar una partidaDet
    public void eliminarPartidaDet(PartidaDetalle pd) throws Exception {
        String query = "exec sp_eliPartidaDet "
                + pd.getIdPartidaDet()
                + ", " + pd.getIdInventarioCrudo();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para obtener la bandera para saber si se puede eliminar un tipo de recorte
    public static int validarBorrarRecorte(PartidaDetalle pd) throws Exception {
        String query= "execute sp_valBorrarRecorte "
                + pd.getIdPartidaDet()
                +", " + pd.getNoPiezas()
                +", " + pd.getIdTipoRecorte()
                +", " + pd.getIdRecortePartidaDet();
        
        int datos = 0;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) 
        {
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                datos = rs.getInt("borrar");
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método que se llama eliminar una partidaDet
    public void eliminarRecorte(PartidaDetalle pd) throws Exception {
        String query = "exec sp_eliRecorte "
                + pd.getIdPartidaDet()
                +", " + pd.getNoPiezas()
                +", " + pd.getIdPartida()
                +", " + pd.getIdRecepcionCuero()
                +", " + pd.getIdTipoRecorte();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para obtener el listado de cuero disponible para agregar a desvenado
    public static List<PartidaDetalle> obtenerCueroEngrase() throws Exception
    {
        List<PartidaDetalle> lstPartidas = new ArrayList<>();
        
        String query = "execute sp_obtCueroEngraseDisp";

        Statement stmt = null;
        ResultSet rs = null;
        
        try 
        {
            c.conectar();
            stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(query);
            rs = stmt.executeQuery(query);

            lstPartidas = new ArrayList<>();
            if (rs.last()) 
            {
                rs.beforeFirst();

                //Recorremos el ResultSet registro a registro
                while (rs.next()) {
                    PartidaDetalle obj = new PartidaDetalle();
                    obj.setIdPartidaDet(rs.getInt("idPartidaDet"));
                    obj.setIdPartida(rs.getInt("idPartida"));
                    obj.setNoPartida(rs.getInt("noPartida"));
                    obj.setRecorte(rs.getString("recorte"));
                    obj.setNoPiezas(rs.getInt("noPiezasAct"));
                    lstPartidas.add(obj);
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
        return lstPartidas;
    }
    
    //Método que se llama para validar si se puede realizar un recorte
    public static int validarCrearRecorte(PartidaDisp pd) throws Exception {
        String query= "execute sp_valCrearRecorte "
                + pd.getIdPartida()
                +", " + pd.getIdRecepcionCuero()
                +", " + pd.getIdTipoRecorte()
                +", " + pd.getIdProceso();
        
        int datos = 0;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) 
        {
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                datos = rs.getInt("bandera");
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para actualizar el número de piezas actuales
    public static void actualizarNoPiezasBaja(BajasPartidaDet bpd) throws Exception {
        String query = "exec sp_actBajasPartidaDet "
                + bpd.getNoPiezas()
                + ", " + bpd.getIdPartidaDet();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para validar la fichaProd a eliminar
    public static int obtenerFichaProdEliminar(FichaProdDet fpd) throws Exception
    {
        String query= "execute sp_obtFichaProdEli "+fpd.getIdFichaProdDet();
        
        int datos = 0;

        c.conectar();
        stmt = c.getConexion().createStatement();
        System.out.println(query);
        
        int count = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        rs = stmt.getGeneratedKeys();
        
        ResultSetMetaData rsmd = rs.getMetaData();
        int contadorColumnas = rsmd.getColumnCount();
        
        if (rs.next()) 
        {
           do 
           {
              for (int i = 1; i <= contadorColumnas; i++) 
              {
                 datos = rs.getInt("validaElimina");
              }
           } 
           while(rs.next());
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para agregar una entrada a la tabla entradaProductoAlmacen
    public static void insPartidaDetFichaReproceso(PartidaDetalle pd) throws Exception {
        String query = "exec sp_InsPartidaDetalleFichaReproceso "
                + pd.getNoPiezas()
                + ", " + pd.getIdPartida()
                + ", " + pd.getIdPartidaDet()
                + ", " + pd.getIdTipoRecorte()
                + ", " + pd.getIdProceso()
                + ", '" + pd.getArea() + "'"
                + ", " + pd.getIdDescontar();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
