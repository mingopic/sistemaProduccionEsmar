/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.EntradaMaterial;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        String strDate = dateFormat.format(em.getFechaEntrada());
        CallableStatement st = null;
        
        String query="execute dbo.Usp_EntradaMaterialCreate ?,?,?,?,?,?"; 
        
        try 
        {
            c.conectar();
            st = c.getConexion().prepareCall(query);
            
            st.setInt(1,em.getMaterialId());
            st.setDouble(2,em.getCantidad());
            st.setString(3,em.getComentarios());
            st.setInt(4,em.getIdUsuario());
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
            st.close();
            c.desconectar();
        }
        return return_value;
    }
    
    public static List<EntradaMaterial> EntradaMaterialGetAll(String codigo, int catDetTipoMaterialId, int catDetClasificacionId, String fechainicio, String fechafin) 
    {
        List<EntradaMaterial> lstEntradaMaterial = null;
        String strDateInicio = null;
        String strDateFin = null;
        
        if (fechainicio != null)
            strDateInicio = "'" + fechainicio + "'";
        
        if (fechafin != null)
            strDateFin = "'" + fechafin + "'";
        
        
        String query = "execute dbo.Usp_EntradasGetAll '" + codigo + "'"
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

            lstEntradaMaterial = new ArrayList<>();
            if (rs.last()) 
            {
                rs.beforeFirst();

                //Recorremos el ResultSet registro a registro
                while (rs.next()) {
                    EntradaMaterial m = new EntradaMaterial();
                    m.setCodigo(rs.getString("Codigo"));
                    m.setDescripcion(rs.getString("Descripcion"));
                    m.setCantidad(rs.getDouble("cantidad"));
                    m.setUnidadMedida(rs.getString("UnidadMedida"));
                    m.setTipoMaterial(rs.getString("TipoMaterial"));
                    m.setClasificacion(rs.getString("Clasificacion"));
                    m.setComentarios(rs.getString("Comentarios"));
                    m.setFechaEntrada(rs.getDate("FechaEntrada"));
                    
                    lstEntradaMaterial.add(m);
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
        return lstEntradaMaterial;
    }
}
