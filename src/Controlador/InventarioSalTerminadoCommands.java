/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Calibre;
import Modelo.Devolucion;
import Modelo.InventarioSalTerminado;
import Modelo.Seleccion;
import Modelo.TipoRecorte;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author EQUIPO-PC
 */
public class InventarioSalTerminadoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método para agregar una entrada a la tabla tb_invsalTerminado
    public static void agregarInvSalTer(InventarioSalTerminado isalt) throws Exception
    {
        String query = "execute sp_agrInvSalTer "
                + isalt.getIdInvTerminado()
                + ", " + isalt.getNoPiezas() 
                + ", " + isalt.getKg()
                + ", " + isalt.getDecimetros()
                + ", " + isalt.getPies()
                + ", " + isalt.getIdCalibre()
                + ", " + isalt.getIdSeleccion()
                + ", " + isalt.getBandera();
        PreparedStatement pstmt = null;
        c.conectar();
        pstmt = c.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        c.desconectar();
    }
    
    //Método para traer las salidas
    public static List<InventarioSalTerminado> ObtenerListaInvSalTerminado() throws Exception
    {
        List<InventarioSalTerminado> lstInvSalTerminado = new ArrayList<>();
        InventarioSalTerminado obj;
        
        String query="execute sp_obtInvSalTer";
        
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
                obj = new InventarioSalTerminado();
                obj.setTipoRecorte(rs.getString("tipoRecorte"));
                obj.setCalibre(rs.getString("calibre"));
                obj.setSeleccion(rs.getString("seleccion"));
                obj.setNoPiezas(rs.getInt("noPiezas"));
                obj.setKg(rs.getDouble("kg"));
                obj.setDecimetros(rs.getDouble("decimetros"));
                obj.setPies(rs.getDouble("pies"));
                
                Date sqlDate = rs.getDate("fechaEntrada");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fecha = sdf.format(sqlDate);
                obj.setFechaEntrada(fecha);
                obj.setBandera(rs.getInt("bandera"));
                obj.setIdInvSalTerminado(rs.getInt("idInvSalTerminado"));
                lstInvSalTerminado.add(obj);
            }
        }
        rs.close();
        stmt.close();
        c.desconectar();
        return lstInvSalTerminado;
    }
}
