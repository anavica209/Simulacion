package ar.edu.itba.simul.group8;

import java.util.ArrayList;
import java.util.List;

public class CellIndexSearch extends NeighborSearch {

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

	public CellIndexSearch(List<Particle> particles, double l, int m) {
		super(particles, l, m);
	}

    public void search(double radius, Neighbors result) {
        Matrix<List<Particle>> matrix = getMatrixFromParticles(particles);

        for (Particle particle : particles) {
            debug("Searching for neighbors of particle (%f; %f) within radius %f", particle.x, particle.y, radius);

            int x = (int) (particle.x / m);
            int y = (int) (particle.y / m);
            int d = (int) Math.ceil(radius / m);

            debug("Particle is in block (%d, %d)", x, y);

            debug("Since block size is %f, we are going to look %d blocks around", l / m, d);

            int xmin = Math.max(0, x - d);
            int ymin = Math.max(0, y - d);

            int xmax = Math.min(m - 1, x + d);
            int ymax = Math.min(m - 1, y + d);

            for (int i = xmin; i <= xmax; i++) {
                for (int j = ymin; j <= ymax; j++) {

                    debug("Searching for particles in cell (%d, %d)", i, j);

                    List<Particle> cell = matrix.get(i, j);

                    for (Particle neighbor : cell) {
                        double dist = distance(particle, neighbor);
                        if (particle != neighbor && dist <= radius) {
                            debug("Neighbor (%f; %f) is within radius (distance: %f)", neighbor.x, neighbor.y, dist);
                            result.addNeighbors(particle, neighbor);
                        } else if (particle != neighbor) {
                            debug("Neighbor (%f; %f) is NOT within radius (distance: %f)", neighbor.x, neighbor.y, dist);
                        }
                    }
                }
            }
        }
	}

    private Matrix<List<Particle>> getMatrixFromParticles(List<Particle> particles) {
        Matrix<List<Particle>> matrix = new Matrix<>(m, m);

		for (int i = 0; i < m; i++){
			for (int j = 0; j < m; j++){
				matrix.put(i, j, new ArrayList<>());
			}
		}

		for (Particle p : particles){
			int x = (int) (p.x / (l / m));
			int y = (int) (p.y / (l / m));

            List<Particle> cell = matrix.get(x, y);
            cell.add(p);
		}

		return matrix;
	}
}
