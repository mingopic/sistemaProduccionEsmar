/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Mingo
 */
public class ConexionBD
{
    private Connection conn;
    
    public ConexionBD()
    {
        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public void conectar() throws Exception
    {
//        String url = "jdbc:sqlserver://EQUIPO-PC:1433;databaseName=esmarProd";
        String url = "jdbc:sqlserver://MINGO-LAP:1433;databaseName=esmarProd";
        String usuario = "sa";
        String password = "root";
        
        //Generamos una conexión solo si no existe alguna ó, si existe y ya está cerrada.
        if (conn == null || conn.isClosed())
            conn = DriverManager.getConnection(url, usuario, password);
    }
    
    public void desconectar() throws Exception 
    {
        conn.close();
    }
    
    public Connection getConexion()
    {
        return conn;
    }
}