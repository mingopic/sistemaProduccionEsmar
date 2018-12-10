/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.TamborCommands.c;
import Modelo.UnidadMedida;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mingo
 */
public class UnidadMedidaCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionBD c=new ConexionBD();
    
    //Método para traer las unidades de medida
    public List obtenerUnidadMedida() throws Exception
    {
        List<UnidadMedida> lstUnidadMedida = new ArrayList<>();
        UnidadMedida obj;
        
        String query="execute sp_obtUnidadMedida";
        
        Statement stmt = null;
        ResultSet rs = null;

        c.conectar();
        stmt = c.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);
        
        if (rs.last()) {
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                obj = new UnidadMedida();
                obj.setIdUnidadMedida(rs.getInt("idUnidadMedida"));
                obj.setDescripcion(rs.getString("descripcion"));
                obj.setDesCorta("desCorta");
                lstUnidadMedida.add(obj);
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return lstUnidadMedida;
    }
}
