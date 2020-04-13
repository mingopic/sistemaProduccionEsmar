/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.CatalogoDet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mingo
 */
public class CatalogoDetCommands {
    static PreparedStatement pstmt;
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método para obtener los datos de la tabla Tb_CatalogoDet
    public static List<CatalogoDet> CatalogoDetGetByCatId(int catId) 
    {
        List<CatalogoDet> lstCatalogoDet = null;
        String query = "execute Usp_CatalogoDetGetByCatId " + catId;

        Statement stmt = null;
        ResultSet rs = null;
        
        try 
        {
            c.conectar();
            stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(query);

            lstCatalogoDet = new ArrayList<>();
            if (rs.last()) 
            {
                rs.beforeFirst();

                //Recorremos el ResultSet registro a registro
                while (rs.next()) {
                    CatalogoDet cd = new CatalogoDet();
                    cd.CatDetId(rs.getInt("CatDetId"));
                    cd.Nombre(rs.getString("Nombre"));
                    cd.Abreviacion(rs.getString("Abreviacion"));
                    cd.OrdenVisualizacion(rs.getInt("OrdenVisualizacion"));
                    cd.Auxiliar(rs.getString("Auxiliar"));
                    cd.FechaUltimaAct(rs.getDate("FechaUltimaAct"));
                    
                    lstCatalogoDet.add(cd);
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
        return lstCatalogoDet;
    }
}
