package ar.edu.itba.simul;

import java.util.ArrayList;
import java.util.List;

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
	@SuppressWarnings("unchecked")
	private static Result algorithmWithoutBorderPerioricalConditions(Object[][] matrix, List<Particle> particles, double ele, int eme, int radioCell, Particle selected){ // N=particulas.size()
		long time= System.currentTimeMillis();
		
		
		loadParticlesInMatrix(matrix, particles);
		
		int i=getXMatrixPosition(selected);
		int j=getYMatrixPosition(selected);
		
		List<Particle> neighboards = new ArrayList<Particle>();
		if(matrix[i][j]!=null){
				for(int x=-radioCell; x<=radioCell;x++){
					for(int y=-radioCell; y<=radioCell; y++){
//						si la coordenada esta dentro del rango de indices de la matriz, evaluar las particulas en esas celdas.
						if(belongs(i, j, x, y, eme)){
							for(Particle p:(List<Particle>)matrix[x][y]){
								if(isNeighboard(radioCell, selected, p)){
									neighboards.add(p);
								}
							}
						}
					}
				}
					
		}
		
		return new Result(neighboards, System.currentTimeMillis()-time);
	}

	private static boolean isNeighboard(int radioCell, Particle selected, Particle p) {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings("unchecked")
	private static void loadParticlesInMatrix(Object[][] matrix, List<Particle> particles) {
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix.length;j++){
				matrix[i][j]=new ArrayList<Particle>();
			}
		}
		for(Particle p: particles){
//			agregar la particula x posicion
			int x=getXMatrixPosition(p);
			int y=getYMatrixPosition(p);
			((List<Particle>)matrix[x][y]).add(p);
		}
	}

	private static int getXMatrixPosition(Particle seleccionada) {
		
		return (int)Math.floor(seleccionada.x);
	}

	private static int getYMatrixPosition(Particle seleccionada) {
		
		return (int)Math.floor(seleccionada.y);
	}

	
	private static boolean belongs(int i, int j, int incx, int incy, int eme) {
		if (i + incx > eme || j + incy > eme)
			return false;
		if (i + incx < 0 || j + incy < 0)
			return false;
		return true;
	}

	
	public static void main(String[] args) {
		int eme=20;
		List<Particle> list=generador(eme);
		
		Object[][] matrix=new Object[eme][eme];
		double ele=0;
		int radiocell=0;
		algorithmWithoutBorderPerioricalConditions(matrix, list, ele, eme, radiocell, list.get(0));
	}

	private static List<Particle> generador(int eme) {
		List<Particle> list=new ArrayList<Particle>();
		for(int i=0; i<eme; i++){
			list.add(new Particle(Math.random()*eme, Math.random()*eme, Math.random()));
		}
		return list;
	}
}
