/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestService;
import RestService.Model.DataSerie;
import RestService.Model.Serie;
import RestService.Model.Response;
import java.net.HttpURLConnection;
import java.net.URL;

//import org.codehaus.jackson.map.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 *
 * @author mingo
 */
public class Series {

    public static Response readSeries(String fecha) {
        try 
        {
            //La URL a consultar con los parametros de idSerie y fechas 
            //Serie SF43787 = Tipo de cambio pesos por dólar E.U.A. Interbancario a 48 horas Apertura compra
            URL url = new URL("https://www.banxico.org.mx/SieAPIRest/service/v1/series/SF43787/datos/"+fecha+"/"+fecha);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //Se realiza una petición GET
            conn.setRequestMethod("GET");
            //Se solicita que la respuesta esté en formato JSON
            conn.setRequestProperty("Content-Type", "application/json");
            //Se envía el header Bmx-Token con el token de consulta
            //Modificar por el token de consulta propio
            conn.setRequestProperty("Bmx-Token", "166f32c02261e097caf3b9af36d5663c06d3b8ceae48ec40729fc33a12746d41");

            //En caso de ser exitosa la petición se devuelve un estatus HTTP 200
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new RuntimeException("HTTP error code : "+ conn.getResponseCode());
            }

            //Se utiliza Jackson para mapear el JSON a objetos Java
            ObjectMapper mapper = new ObjectMapper();
            Response response=mapper.readValue(conn.getInputStream(), Response.class);

            conn.disconnect();

            return response;   
        } 
        catch (Exception e) 
        {
            return null;
        }

    }

    /*
    public static void main(String[] args) {
            try {
                    Response response=readSeries();
                    Serie serie=response.getBmx().getSeries().get(0);
                    System.out.println("Serie: "+serie.getTitulo());
                    for(DataSerie data:serie.getDatos()){
                            //Se omiten las observaciones sin dato (N/E)
                            if(data.getDato().equals("N/E")) continue;
                            System.out.println("Fecha: "+data.getFecha());
                            System.out.println("Dato: "+data.getDato());
                    }

            } catch(Exception e) {
                    System.out.println("ERROR: "+e.getMessage());
            }
    }
    */
}