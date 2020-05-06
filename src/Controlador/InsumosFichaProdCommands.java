/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Dto.RespuestaDto;
import Modelo.Entity.InsumosFichaProd;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Mingo
 */
public class InsumosFichaProdCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para obtener el último IdInsumoFichaProd
    public static int obtenerUltIdInsumoFichaProd() throws Exception
    {
        int idInsumoFichaProd = 0;
        
        String query = "execute sp_ObtUltIdInsumoFichaProd";
        
        Statement stmt = null;
        ResultSet rs = null;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            rs.beforeFirst();
            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                idInsumoFichaProd = rs.getInt("idInsumoFichaProd");
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return idInsumoFichaProd;
    }
    
    //Método para agregar un registro a la tabla tb_InsumoFichaProd
    public static void agregarInsumosFichaProd(InsumosFichaProd ifp) throws Exception {
        String query = "execute sp_InsInsumosFichaProd "
                + ifp.getIdFichaProd()
                + ", " + ifp.getIdProceso()
                + ", " + ifp.getIdSubproceso()
                + ", " + ifp.getIdFormXSubProc()
                + ", " + ifp.getTotalInsumos()
                + ", " + ifp.getCatDetEstatusSurtidoId();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    public RespuestaDto ValidarSurtido(int idFichaProd) throws Exception
    {
        RespuestaDto respuesta = new RespuestaDto();
        CallableStatement st = null;
        
        String query="execute dbo.Usp_InsumosFichaProdValidarSurtido ?,?,?"; 
        
        //System.out.println("execute dbo.Usp_InsumosFichaProdValidarSurtido " + idFichaProd);
         
        try 
        {
            c.conectar();
            st = c.getConexion().prepareCall(query);
            
            st.setInt(1,idFichaProd);
            st.registerOutParameter(2, java.sql.Types.INTEGER);
            st.registerOutParameter(3, java.sql.Types.VARCHAR);
            
            st.execute();
            respuesta.setRespuesta(st.getInt("Respuesta"));
            respuesta.setMensaje(st.getString("Mensaje"));
        } 
        catch (SQLException e) 
        {
            System.err.println(e);
            respuesta.setRespuesta(-1);
            respuesta.setMensaje("Error al buscar ficha de producción, " + e.getMessage());
        }
        finally
        {
            st.close();
            c.desconectar();
        }
        return respuesta;
    }
}
