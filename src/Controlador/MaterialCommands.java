/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.Material;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mingo
 */
public class MaterialCommands {
    static PreparedStatement pstmt;
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método para obtener los datos de la tabla Tb_Material
    public static List<Material> MaterialGetAll() 
    {
        List<Material> lstMaterial = null;
        String query = "execute Usp_MaterialGetAll";

        Statement stmt = null;
        ResultSet rs = null;
        
        try 
        {
            c.conectar();
            stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(query);

            lstMaterial = new ArrayList<>();
            if (rs.last()) 
            {
                rs.beforeFirst();

                //Recorremos el ResultSet registro a registro
                while (rs.next()) {
                    Material m = new Material();
                    m.MaterialId(rs.getInt("MaterialId"));
                    m.Codigo(rs.getString("Codigo"));
                    m.Descripcion(rs.getString("Descripcion"));
                    m.Existencia(rs.getDouble("Existencia"));
                    m.IdUnidadMedida(rs.getInt("idUnidadMedida"));
                    m.Precio(rs.getDouble("Precio"));
                    m.IdTipoMoneda(rs.getInt("idTipoMoneda"));
                    m.CatDetTipoMaterialId(rs.getInt("CatDetTipoMaterialId"));
                    m.CatDetClasificacionId(rs.getInt("CatDetClasificacionId"));
                    m.CatDetEstatusId(rs.getInt("CatDetEstatusId"));
                    m.FechaUltimaAct(rs.getString("FechaUltimaAct"));
                    
                    m.UnidadMedida(rs.getString("UnidadMedida"));
                    m.TipoMoneda(rs.getString("TipoMoneda"));
                    m.TipoMaterial(rs.getString("TipoMaterial"));
                    m.Clasificacion(rs.getString("Clasificacion"));
                    m.Estatus(rs.getString("Estatus"));
                    
                    lstMaterial.add(m);
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
        return lstMaterial;
    }
}
