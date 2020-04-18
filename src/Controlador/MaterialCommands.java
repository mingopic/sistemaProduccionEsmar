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
                    m.setMaterialId(rs.getInt("MaterialId"));
                    m.setCodigo(rs.getString("Codigo"));
                    m.setDescripcion(rs.getString("Descripcion"));
                    m.setExistencia(rs.getDouble("Existencia"));
                    m.setIdUnidadMedida(rs.getInt("idUnidadMedida"));
                    m.setPrecio(rs.getDouble("Precio"));
                    m.setIdTipoMoneda(rs.getInt("idTipoMoneda"));
                    m.setCatDetTipoMaterialId(rs.getInt("CatDetTipoMaterialId"));
                    m.setCatDetClasificacionId(rs.getInt("CatDetClasificacionId"));
                    m.setCatDetEstatusId(rs.getInt("CatDetEstatusId"));
                    m.setFechaUltimaAct(rs.getString("FechaUltimaAct"));
                    
                    m.setUnidadMedida(rs.getString("UnidadMedida"));
                    m.setTipoMoneda(rs.getString("TipoMoneda"));
                    m.setTipoMaterial(rs.getString("TipoMaterial"));
                    m.setClasificacion(rs.getString("Clasificacion"));
                    m.setEstatus(rs.getString("Estatus"));
                    
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
