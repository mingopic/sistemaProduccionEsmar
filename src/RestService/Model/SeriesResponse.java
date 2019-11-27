/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestService.Model;

/**
 *
 * @author mingo
 */
import java.util.List;

public class SeriesResponse {

	private List<Serie>series;

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series = series;
	}
	
}