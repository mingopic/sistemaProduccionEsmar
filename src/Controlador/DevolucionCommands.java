/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Entity.Calibre;
import Modelo.Entity.Devolucion;
import Modelo.Entity.Seleccion;
import Modelo.Entity.TipoRecorte;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author SISTEMAS
 */
public class DevolucionCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para traer las devoluciones
    public static List<Devolucion> ObtenerListaDevoluciones(TipoRecorte tr, Calibre ca, Seleccion s, Devolucion d) throws Exception
    {
        List<Devolucion> lstDevolucion = new ArrayList<>();
        Devolucion obj;
        
        String query="execute sp_ObtDevoluciones "
                + "'" + tr.getDescripcion()+"'"
                + "," + "'"+ ca.getDescripcion() +"'"
                + "," + "'"+ s.getDescripcion() +"'"
                + "," + "'"+ d.getFecha()+"'"
                + "," + "'"+ d.getFecha1()+"'";
        
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
                obj = new Devolucion();
                obj.setIdDevolucion(rs.getInt("idDevolucion"));
                obj.setTipoRecorte(rs.getString("tipoRecorte"));
                obj.setNoPiezas(rs.getInt("noPiezas"));
                obj.setKg(rs.getDouble("kg"));
                obj.setDecimetros(rs.getDouble("decimetros"));
                obj.setPies(rs.getDouble("pies"));
                obj.setCalibre(rs.getString("calibre"));
                obj.setSeleccion(rs.getString("seleccion"));
                obj.setMotivo(rs.getString("motivo"));
                
                Date sqlDate = rs.getDate("fecha");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                obj.setFecha(sdf.format(sqlDate));
                lstDevolucion.add(obj);
            }
        }
        rs.close();
        stmt.close();
        c.desconectar();
        return lstDevolucion;
    }
    
    //Método para agregar una entrada a la tabla td_devoluciones
    public static void agregarDevolucion(Devolucion d) throws Exception
    {
        String query = "execute sp_agrDevolucion "
                + d.getIdInvSalTerminado()
                + "," + d.getNoPiezas()
                + ",'"+ d.getMotivo()
                + "',"+ d.getBandera()
                + "," + d.getKg()
                + "," + d.getDecimetros()
                + "," + d.getPies();
        
        PreparedStatement pstmt = null;
        c.conectar();
        System.out.println(query);
        pstmt = c.getConexion().prepareStatement(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
}
