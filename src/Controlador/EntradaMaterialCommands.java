/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.EntradaMaterial;
import Modelo.Entity.Material;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mingo
 */
public class EntradaMaterialCommands {
    static PreparedStatement pstmt;
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método insertar una entrada de material
    public int EntradaMaterialCreate(EntradaMaterial em) throws Exception
    {
        int return_value = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(em.FechaEntrada());
        CallableStatement st = null;
        
        String query="execute Usp_EntradaMaterialCreate ?,?,?,?,?,?"; //moved out to parameters to avoid SQL injection.       
        
        try 
        {
            c.conectar();
            st = c.getConexion().prepareCall(query);
            
            st.setInt(1,em.MaterialId());
            st.setDouble(2,em.Cantidad());
            st.setString(3,em.Comentarios());
            st.setInt(4,em.IdUsuario());
            st.setString(5,strDate);
            st.registerOutParameter(6, java.sql.Types.INTEGER);  
            
            st.execute();
            return_value = st.getInt("Return_value");
        } 
        catch (SQLException e) 
        {
            System.err.println(e);
        }
        finally
        {
            c.desconectar();
        }
        return return_value;
    }
}
