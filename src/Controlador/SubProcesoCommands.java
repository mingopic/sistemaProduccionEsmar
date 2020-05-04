/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;
import Modelo.Entity.InsumoPorProceso;
import Modelo.Entity.InsumosXFichaProd;
import Modelo.Entity.Proceso;
import Modelo.Entity.SubProceso;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Equipo
 */
public class SubProcesoCommands {
    static Statement stmt = null;
    static PreparedStatement pstmt;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    static SubProcesoCommands subP;
    
    //Método que se llama para obtener la lista de los cueros por trabajar
    public static String[][] obtenerListaSubprocesosXid(Proceso pr) throws Exception {
        String query= "execute sp_obtSubProcXid "+pr.getIdProceso();
        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 2;
        int i = 0;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) 
        {
            renglones = rs.getRow();
            datos = new String[renglones][columnas];
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                datos[i][0] = rs.getString("descripcion");
                datos[i][1] = rs.getString("idSubProceso");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método que se llama para obtener la lista de las formulas pertenecientes al subProceso señalado
    public static List<InsumoPorProceso> obtenerListaInsXSubProc(SubProceso subP) throws Exception {
        List<InsumoPorProceso> lstInsumoPorProceso = null;
        Statement st = null;
        ResultSet rs = null;
        
        String query = "execute sp_obtFormInsXSubProc "+subP.getIdSubProceso()+";";

        try 
        {
            c.conectar();
            st = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery(query);
            lstInsumoPorProceso = new ArrayList<>();

            if (rs.last()) 
            {
                rs.beforeFirst();

                //Recorremos el ResultSet registro a registro
                while (rs.next()) 
                {
                    InsumoPorProceso insumoPorProceso = new InsumoPorProceso();
                    insumoPorProceso.setIdInsumXProc(rs.getInt("idInsumXProc"));
                    insumoPorProceso.setIdFormXSubProc(rs.getInt("idFormXSubProc"));
                    insumoPorProceso.setClave(rs.getString("clave"));
                    insumoPorProceso.setPorcentaje(rs.getFloat("porcentaje"));
                    insumoPorProceso.setIdInsumo(rs.getInt("idInsumo"));
                    insumoPorProceso.setNombreProducto(rs.getString("nombreProducto"));
                    insumoPorProceso.setComentario(rs.getString("comentario"));
                    insumoPorProceso.setMaterialId(rs.getInt("MaterialId"));
                    insumoPorProceso.setCatDetOrigenMaterialId(rs.getInt("CatDetOrigenMaterialId"));
                    lstInsumoPorProceso.add(insumoPorProceso);

                    /*
                    datos[i][0] = rs.getString("clave");

                    if (rs.getString("idInsumo").equals("0"))
                        datos[i][1] = "";
                    else
                        datos[i][1] = rs.getString("porcentaje");

                    datos[i][2] = rs.getString("nombreProducto");
                    datos[i][3] = rs.getString("comentario");
                    datos[i][4] = rs.getString("idInsumo");
                    datos[i][5] = rs.getString("idFormXSubProc");
                    datos[i][6] = rs.getString("idInsumXProc");
                    */
                }
            }
            
        } 
        catch (Exception e) 
        {
            System.err.println(e);
        }
        finally
        {
            rs.close();
            st.close();
            c.desconectar();
        }
        return lstInsumoPorProceso;
    }
    
    //Método que se llama para obtener la fórmula de insumos del subproceso seleccionado
    public static List<InsumosXFichaProd> obtInsXSubProcList(int idSubProceso) throws Exception 
    {
        List<InsumosXFichaProd> lstInsumos = new ArrayList<>();
        
        String query = "execute sp_obtFormInsXSubProc "+idSubProceso;

        Statement stmt = null;
        ResultSet rs = null;
        
        try 
        {
            c.conectar();
            stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(query);
            rs = stmt.executeQuery(query);

            lstInsumos = new ArrayList<>();
            if (rs.last()) 
            {
                rs.beforeFirst();

                //Recorremos el ResultSet registro a registro
                while (rs.next()) {
                    InsumosXFichaProd obj = new InsumosXFichaProd();
                    if (rs.getInt("idInsumo") == 0)
                    {
                        obj.setPorcentaje(0.0);
                        obj.setClave("");
                    }
                    else
                    {
                        obj.setPorcentaje(rs.getDouble("porcentaje"));
                        obj.setClave(rs.getString("clave"));
                    }
                    obj.setMaterial(rs.getString("nombreProducto"));
                    obj.setRodar(rs.getString("comentario"));
                    obj.setIdProducto(rs.getInt("idInsumo"));
                    obj.setIdFormXSubProc(rs.getInt("IdFormXSubProc"));
                    obj.setTemperatura("");
                    obj.setPrecioUnitario(Double.parseDouble(String.format("%.2f",rs.getDouble("precioUnitario"))));
                    lstInsumos.add(obj);
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
        return lstInsumos;
    }
    
    //Método que se llama para obtener la fórmula de insumos del subproceso seleccionado
    public static Double obtPrecioProducto(int idProducto) throws Exception 
    {
        String query = "execute sp_Compaq_ObtPrecioInsumo "+idProducto;

        Statement stmt = null;
        ResultSet rs = null;
        Double precioProducto = 0.0;
        
        try 
        {
            c.conectarCompaq();
            stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(query);
            rs = stmt.executeQuery(query);

            if (rs.last()) 
            {
                rs.beforeFirst();

                //Recorremos el ResultSet registro a registro
                while (rs.next()) {
                    precioProducto = (Double.parseDouble(String.format("%.2f",rs.getDouble("precio"))));
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
        return precioProducto;
    }
    
    //Método para obtener todos los subprocesos registrados de BD
    public static String[][] obtenerSubprocesos(String proceso) throws Exception {
        String query= "EXEC sp_obtSubProc '"+proceso+"'";

        String[][] datos = null;
        int renglones = 0;
        int columnas = 2;
        int i = 0;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) 
        {
            renglones = rs.getRow();
            datos = new String[renglones][columnas];
            rs.beforeFirst();
            
            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                datos[i][0] = rs.getString("subProceso");
                datos[i][1] = rs.getString("proceso");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para buscar datos de un subProceso en BD
    public static SubProceso obtSubProcXdesc(String subProceso) throws Exception
    {   
        SubProceso sp = new SubProceso();
        String query="execute sp_obtSubProcXdesc '"+subProceso+"'";

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.next())
        {
            sp.setIdSubProceso(Integer.parseInt(rs.getString("idSubProceso")));
            sp.setDescripcion(rs.getString("descripcion"));
            sp.setIdProceso(Integer.parseInt(rs.getString("idProceso")));
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return sp;
    }
    
    //Método que se llama para insertar un subProceso
    public static void insertarSubProceso(SubProceso s, String proceso) throws Exception {
        String query= "execute sp_insSubProc '"+s.getDescripcion()+"', '"+proceso+"'";
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método que se llama para actualizar detos de un subProceso
    public static void actualizarSubProceso(SubProceso s, String proceso) throws Exception {
        String query= "execute sp_actSubProc '"+s.getDescripcion()+"', '"+proceso+"', "+s.getIdSubProceso();
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para buscar datos de un subProceso en BD
    public String obtenerSubProcesoXidFichaProd(int idFichaProd) throws Exception
    {   
        String query="execute sp_ObtSubProcesoXfichaProd " + idFichaProd;
        String subproceso = "";
        
        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(query);
        rs = stmt.executeQuery(query);
        
        if (rs.next())
        {
            subproceso = (rs.getString("descripcion"));
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return subproceso;
    }
    
    //Método para traer los subprocesos activos
    public List llenarComboboxSubProcesos() throws Exception
    {
        List<SubProceso> lstSubProceso = new ArrayList<>();
        SubProceso obj;
        
        String query="execute sp_obtSubProcesos";
        
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
                obj = new SubProceso();
                obj.setIdSubProceso(Integer.parseInt(rs.getString("idSubproceso")));
                obj.setDescripcion(rs.getString("descripcion"));
                lstSubProceso.add(obj);
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return lstSubProceso;
    }
}
