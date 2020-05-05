/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Dto.RespuestaDto;
import Modelo.Entity.Material;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public static List<Material> MaterialGetAll(Material mat) 
    {
        List<Material> lstMaterial = null;
        String query = "execute Usp_MaterialGetAll '" + mat.getCodigo() + "'"
                        + ", " + mat.getCatDetTipoMaterialId()
                        + ", " + mat.getCatDetClasificacionId();

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
    
    //Método para obtener los datos de la tabla Tb_Material
    public static List<Material> MaterialGetCollectionByCatDetTipoMaterialId(int catDetTipoMaterialId) 
    {
        List<Material> lstMaterial = null;
        Statement st = null;
        ResultSet rs = null;
        
        String query = "execute Usp_MaterialGetCollectionByCatDetTipoMaterialId " + catDetTipoMaterialId;

        try 
        {
            c.conectar();
            st = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);

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
            st.close();
            c.desconectar();
        } 
        catch (Exception e) 
        {
            System.err.println(e);
        }
        return lstMaterial;
    }
    
    //Método para insertar un material
    public RespuestaDto MaterialCreate(Material m) throws Exception
    {
        RespuestaDto respuesta = new RespuestaDto();
        CallableStatement st = null;
        
        String query="execute dbo.Usp_MaterialCreate ?,?,?,?,?,?,?,?,?,?"; 
        
        System.out.println("execute dbo.Usp_MaterialCreate "
                + "'" + m.getCodigo() + "'"
                + ", '" + m.getDescripcion()+ "'"
                + ", " + m.getExistencia()
                + ", " + m.getIdUnidadMedida()
                + ", " + m.getPrecio()
                + ", " + m.getIdTipoMoneda()
                + ", " + m.getCatDetTipoMaterialId()
                + ", " + m.getCatDetClasificacionId());
         
        try 
        {
            c.conectar();
            st = c.getConexion().prepareCall(query);
            
            st.setString(1,m.getCodigo());
            st.setString(2,m.getDescripcion());
            st.setDouble(3,m.getExistencia());
            st.setInt(4,m.getIdUnidadMedida());
            st.setDouble(5,m.getPrecio());
            st.setInt(6, m.getIdTipoMoneda());
            st.setInt(7, m.getCatDetTipoMaterialId());
            st.setInt(8, m.getCatDetClasificacionId());
            st.registerOutParameter(9, java.sql.Types.INTEGER);
            st.registerOutParameter(10, java.sql.Types.VARCHAR);
            
            st.execute();
            respuesta.setRespuesta(st.getInt("Respuesta"));
            respuesta.setMensaje(st.getString("Mensaje"));
        } 
        catch (SQLException e) 
        {
            System.err.println(e);
            respuesta.setRespuesta(-1);
            respuesta.setMensaje("Error al guardar material, " + e.getMessage());
        }
        finally
        {
            st.close();
            c.desconectar();
        }
        return respuesta;
    }
}
