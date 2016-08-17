package simu1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellIndexMethod {

	// 1- Implementar el algoritmo "Cell Index Method" que tome como inputs: las
	// posiciones y radios de
	// las N partículas y los parámetros N, L, M y r c (ver punto 5), y cuyos
	// outputs sean:
	// - Una lista tal que para cada partícula indique cuales son las vecinas
	// que distan menos de r c .
	// - El tiempo de ejecución.
	// - Además se debe generar una figura que muestre las posiciones de todas
	// las partículas, y que
	// identifique una de ellas (pasada como input) de un color y sus vecinos
	// correspondientes de otro
	// color.
	// Las distancias entre partículas deben medirse borde a borde, es decir,
	// considerando el radio (r)
	// además del centro de masa de las mismas. Cómo se modifica el criterio L/M
	// > r c cuando la
	// partícula no es puntual, es decir tiene un radio (r) ? (podría suceder
	// que el centro esté en una celda
	// no vecina pero el borde sí esté en la vecina).
	//
	// Como parámetro adicional considerar dos versiones del algoritmo:
	// a- Sin condiciones periódicas de contorno (considerando distancia a los
	// bordes del área: paredes).
	//
	private Result algoritmoSinCPC(Object[][] matriz, List<Particula> particulas, double ele, int eme, int radioCell, Particula seleccionada){ // N=particulas.size()
		long time= System.currentTimeMillis();
		Map<Particula, List<Particula>> m=new HashMap<Particula, List<Particula>>();
		
		
		asignarParticulasAMatriz(matriz, particulas);
		
		int i=obtenerPosicionEnMatrizX(seleccionada);
		int j=obtenerPosicionEnMatrizY(seleccionada);
		
		if(matriz[i][j]!=null){
			@SuppressWarnings("unchecked")
			List<Particula> lista = (List<Particula>)matriz[i][j];

			for(Particula particula: lista){
				List<Particula> vecinos = new ArrayList<Particula>();
				for(int x=-radioCell; x<=radioCell;x++){
					for(int y=-radioCell; y<=radioCell; y++){
//						si la coordenada esta dentro del rango de indices de la matriz, evaluar las particulas en esas celdas.
						if(perteneceMatriz(i, j, x, y, eme)){
//									considerar criterios para agregado de particulas con areas, 
//									se agrega una particula si su centro se encuentra fuera de la zona pero entra por el radio?
							vecinos.addAll((List<Particula>)matriz[i+x][j+y]);
						}
					}
				}
				
				m.put(particula, vecinos);
			}
					
		}
		
		return new Result(m, System.currentTimeMillis()-time);
	}

	private int obtenerPosicionEnMatrizX(Particula seleccionada) {
		
		return (int)Math.floor(seleccionada.getPosicion().getX());
	}

	private int obtenerPosicionEnMatrizY(Particula seleccionada) {
		
		return (int)Math.floor(seleccionada.getPosicion().getY());
	}

	
	private boolean perteneceMatriz(int i, int j, int incx, int incy, int eme) {
		if (i + incx > eme || j + incy > eme)
			return false;
		if (i + incx < 0 || j + incy < 0)
			return false;
		return true;
	}

}
