/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Reproceso;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SISTEMAS
 */
public class ReprocesoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c = new ConexionBD();
    
    //Método que se llama para obtener el número de partidas registradas de las diferentes areas
    public static List<Reproceso> obtenerPartidasDisponibles(Reproceso r) 
    {
        List<Reproceso> reproceso = null;
        String query = "execute sp_obtPartidaXarea '"+r.getArea()+"'";

        Statement stmt = null;
        ResultSet rs = null;
        
        try 
        {
            c.conectar();
            stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(query);
            rs = stmt.executeQuery(query);

            reproceso = new ArrayList<>();
            if (rs.last()) 
            {
                rs.beforeFirst();

                //Recorremos el ResultSet registro a registro
                while (rs.next()) {
                    Reproceso obj = new Reproceso();
                    obj.setNoPartida(rs.getInt("NoPartida"));
                    obj.setProveedorCamion(rs.getString("Proveedor"));
                    obj.setTipoRecorte(rs.getString("descripcion"));
                    obj.setNoPiezasActuales(rs.getInt("noPiezasAct"));
                    obj.setIdPartidaDet(rs.getInt("idPartidaDet"));
                    obj.setIdPartida(rs.getInt("idPartida"));
                    obj.setIdTipoRecorte(rs.getInt("idTipoRecorte"));
                    obj.setIdDescontar(rs.getInt("idDescontar"));
                    reproceso.add(obj);
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
        return reproceso;
    }
}
