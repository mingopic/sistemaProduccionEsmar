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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                if (Integer.parseInt(rs.getString("Return_value")) > 0) {
                    return_value = Integer.parseInt(rs.getString("Return_value"));
                }else{
                    return -1;
                }
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
    
    public int SalidaFichaMaterialCreate(ArrayList<SalidaMaterial> sm, int idFichaProd) throws Exception {
        int return_value = -1;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CallableStatement st = null;
        try {
            c.conectar();
            String query = "execute dbo.Usp_SalidaMaterialCreate ?,?,?,?,?,?,?,?";
            for (SalidaMaterial material : sm) {
                String strDate = dateFormat.format(material.getFechaSalida());

                st = c.getConexion().prepareCall(query);
                st.setInt(1, material.getMaterialId());
                st.setDouble(2, material.getCantidad());
                st.setString(3, material.getSolicitante());
                st.setString(4, material.getDepartamento());
                st.setString(5, material.getComentarios());
                st.setInt(6, material.getIdInsumoFichaProd());
                st.setInt(7, material.getIdUsuario());
                st.setString(8, strDate);
                //st.registerOutParameter(9, java.sql.Types.INTEGER);  
                rs = st.executeQuery();
                if (rs.next()) {
                    return_value = Integer.parseInt(rs.getString("Return_value"));
                }
            }
            if (return_value > 0) {

                query = "execute dbo.Usp_InsumosFichaProdUpdateEstatusSurtido ?";
                st = c.getConexion().prepareCall(query);
                st.setInt(1, idFichaProd);
                rs = st.executeQuery();
                if (rs.next()) {
                    return_value = Integer.parseInt(rs.getString("Return_value"));
                }

            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            rs.close();
            st.close();
            c.desconectar();
        }
        return return_value;
    }
    
    //Método para editar una salida de material
    public int SalidaMaterialUpdate(SalidaMaterial sm) throws Exception
    {
        int return_value = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(sm.getFechaSalida());
        CallableStatement st = null;
        
        String query="execute dbo.Usp_SalidaMaterialUpdate ?,?,?,?,?,?,?,?"; 
        
        try 
        {
            c.conectar();
            st = c.getConexion().prepareCall(query);
            
            st.setInt(1,sm.getSalidaMaterialId());
            st.setDouble(2,sm.getCantidad());
            st.setString(3,sm.getSolicitante());
            st.setString(4,sm.getDepartamento());
            st.setString(5,sm.getComentarios());
            st.setInt(6,sm.getIdUsuario());
            st.setString(7,strDate);
            st.registerOutParameter(8, java.sql.Types.INTEGER);  
            
            st.execute();
            return_value = st.getInt("Return_value");
        } 
        catch (SQLException e) 
        {
            System.err.println(e);
        }
        finally
        {
            st.close();
            c.desconectar();
        }
        return return_value;
    }
    
    public int SalidaMaterialDelete(int salidaMaterialId) throws Exception
    {
        int return_value = 0;
        CallableStatement st = null;
        
        String query="execute dbo.Usp_SalidaMaterialDelete ?,?"; 
        
        try 
        {
            c.conectar();
            st = c.getConexion().prepareCall(query);
            
            st.setInt(1,salidaMaterialId);
            st.registerOutParameter(2, java.sql.Types.INTEGER);  
            
            st.execute();
            return_value = st.getInt("Return_value");
        } 
        catch (SQLException e) 
        {
            System.err.println(e);
        }
        finally
        {
            st.close();
            c.desconectar();
        }
        return return_value;
    }
    
    //Método para obtener los datos de la tabla Tb_Material
    public static List<Map> MaterialGetCollectionByIdInsumoFichaProd(int idInsumoFichaProd) 
    {
        List<Map> lstMaterial = null;
        Statement st = null;
        ResultSet rs = null;
        
        String query = "execute Usp_MaterialGetCollectionByIdInsumoFichaProd " + idInsumoFichaProd;

        try 
        {
            c.conectar();
            st = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);

            lstMaterial = new ArrayList<>();
                //Recorremos el ResultSet registro a registro
                while (rs.next()) {
                    Map m = new HashMap();
                    m.put("idInsumoFichaProd",rs.getInt("idInsumoFichaProd"));
                    m.put("MaterialId",rs.getInt("MaterialId"));
                    m.put("Codigo",rs.getString("Codigo"));
                    m.put("Material",rs.getString("Material"));
                    m.put("UnidadMedida",rs.getString("UnidadMedida"));
                    m.put("CantidadSolicitada",rs.getFloat("CantidadSolicitada"));
                    m.put("Existencia",rs.getFloat("Existencia"));
                    m.put("CantidadSuficiente",rs.getInt("CantidadSuficiente"));
                    lstMaterial.add(m);
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
    
    public static List<SalidaMaterial> SalidaMaterialGetAll(String codigo, int catDetTipoMaterialId, int catDetClasificacionId, String fechainicio, String fechafin) 
    {
        List<SalidaMaterial> lstSalidaMaterial = null;
        String strDateInicio = null;
        String strDateFin = null;
        
        if (fechainicio != null)
            strDateInicio = "'" + fechainicio + "'";
        
        if (fechafin != null)
            strDateFin = "'" + fechafin + "'";
        
        
        String query = "execute dbo.Usp_SalidasGetAll '" + codigo + "'"
                        + ", " + catDetTipoMaterialId
                        + ", " + catDetClasificacionId
                        + ", " + strDateInicio + ""
                        + ", " + strDateFin + "";
        
        //System.out.println(query);

        Statement stmt = null;
        ResultSet rs = null;
        
        try 
        {
            c.conectar();
            stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(query);

            lstSalidaMaterial = new ArrayList<>();
            if (rs.last()) 
            {
                rs.beforeFirst();

                //Recorremos el ResultSet registro a registro
                while (rs.next()) {
                    SalidaMaterial m = new SalidaMaterial();
                    m.setSalidaMaterialId(rs.getInt("SalidaMaterialId"));
                    m.setMaterialId(rs.getInt("MaterialId"));
                    m.setCodigo(rs.getString("Codigo"));
                    m.setDescripcion(rs.getString("Descripcion"));
                    m.setUnidadMedida(rs.getString("UnidadMedida"));
                    m.setCantidad(rs.getDouble("Cantidad"));
                    m.setTipoMaterial(rs.getString("TipoMaterial"));
                    m.setClasificacion(rs.getString("Clasificacion"));
                    m.setFicha(rs.getInt("Ficha"));
                    m.setSolicitante(rs.getString("Solicitante"));
                    m.setDepartamento(rs.getString("Departamento"));
                    m.setComentarios(rs.getString("Comentarios"));
                    m.setFechaSalida(rs.getDate("FechaSalida"));
                    
                    lstSalidaMaterial.add(m);
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
        return lstSalidaMaterial;
    }
}
