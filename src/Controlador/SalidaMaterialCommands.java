/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.SalidaMaterial;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Mingo
 */
public class SalidaMaterialCommands {
    static PreparedStatement pstmt;
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método para insertar una salida de material
    public int SalidaMaterialCreate(SalidaMaterial sm) throws Exception
    {
        int return_value = -1;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(sm.getFechaSalida());
        CallableStatement st = null;
        
        String query="execute dbo.Usp_SalidaMaterialCreate ?,?,?,?,?,?,?,?"; 
        
        /*
        System.out.println("execute dbo.Usp_SalidaMaterialCreate " 
                + sm.getMaterialId()
                + ", " + sm.getCantidad()
                + ", '" + sm.getSolicitante() + "'"
                + ", '" + sm.getDepartamento() + "'"
                + ", '" + sm.getComentarios() + "'"
                + ", " + sm.getIdInsumoFichaProd()
                + ", " + sm.getIdUsuario()
                + ", '" + strDate + "'");
        */
        
        try 
        {
            c.conectar();
            st = c.getConexion().prepareCall(query);
            
            st.setInt(1,sm.getMaterialId());
            st.setDouble(2,sm.getCantidad());
            st.setString(3,sm.getSolicitante());
            st.setString(4,sm.getDepartamento());
            st.setString(5,sm.getComentarios());
            st.setInt(6, sm.getIdInsumoFichaProd());
            st.setInt(7, sm.getIdUsuario());
            st.setString(8, strDate);
            //st.registerOutParameter(9, java.sql.Types.INTEGER);  
            
            rs = st.executeQuery();
            if (rs.next())
            {
                return_value = Integer.parseInt(rs.getString("Return_value"));
            }
            
        } 
        catch (SQLException e) 
        {
            System.err.println(e);
        }
        finally
        {
            rs.close();
            st.close();
            c.desconectar();
        }
        return return_value;
    }
}
