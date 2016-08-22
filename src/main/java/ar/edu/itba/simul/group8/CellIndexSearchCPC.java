package ar.edu.itba.simul.group8;

import java.util.ArrayList;
import java.util.List;

public class CellIndexSearchCPC extends NeighborSearch {

	private Matrix<List<Particle>> matrix;

	public CellIndexSearchCPC(List<Particle> particles, double l, int m) {
		super(particles, l, m);
	}

	@Override
	public void init() {
		matrix = getMatrixFromParticles(particles);
	}

	@Override
	public void search(double radius, Neighbors result) {
		for (Particle particle : particles) {
			debug("Searching for neighbors of particle (%f; %f) within radius %f", particle.x, particle.y, radius);

			int x = (int) (particle.x / m);
			int y = (int) (particle.y / m);
			int d = (int) Math.ceil(radius / m);

			debug("Particle is in block (%d, %d)", x, y);

			debug("Since block size is %f, we are going to look %d blocks around", l / m, d);
			List<Particle> cell;
			// caso dentro todas las celdas dentro de la matriz
			if (x - d >= 0 && x + d < m && y - d >= 0 && y + d < m) {
				for (int xm = -d; xm <= d; xm++) {
					for (int ym = -d; ym <= d; ym++) {
						cell = matrix.get(x + xm, y + ym);
						addNeighborsOfSideCell(cell, particle, particle.x, particle.y, radius, result);
					}
				}
				// caso lateral superior
			} else if (x - d >= 0 && x + d < m && y - d < 0 && y + d < m) {

				for (int xm = -d; xm <= d; xm++) {
					for (int ym = -d; ym <= d; ym++) {
						if(y + ym < 0){
							cell =  matrix.get(x + xm, m + y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x, particle.y + l,radius, result);
						}else{
							cell = matrix.get(x + xm, y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x, particle.y, radius, result);
						}
					}
				}
				// caso lateral inferior
			} else if (x - d >= 0 && x + d < m && y - d >= 0 && y + d >= m) {

				for (int xm = -d; xm <= d; xm++) {
					for (int ym = -d; ym <= d; ym++) {
						if(y + ym >= m){
							cell =  matrix.get(x + xm, y + ym - m);
							addNeighborsOfSideCell(cell, particle, particle.x, particle.y - l,radius, result);
						}else{
							cell = matrix.get(x + xm, y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x, particle.y, radius, result);
						}
					}
				}
				// caso lateral izq
			} else if (x - d < 0 && x + d < m && y - d >= 0 && y + d < m) {
				for (int xm = -d; xm <= d; xm++) {
					for (int ym = -d; ym <= d; ym++) {
						
						if(x + xm < 0){
							cell =  matrix.get(m + x + xm, y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x + l , particle.y,radius, result);
						}else{
							cell = matrix.get(x + xm, y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x, particle.y, radius, result);
						}
					}
				}
				// caso lateral derecho
			} else if (x - d >= 0 && x + d >= m && y - d >= 0 && y + d < m) {

				for (int xm = -d; xm <= d; xm++) {
					for (int ym = -d; ym <= d; ym++) {
						
						if(x + xm >= m){
							cell =  matrix.get(x + xm - m, y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x - l , particle.y,radius, result);
						}else{
							cell = matrix.get(x + xm, y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x, particle.y, radius, result);
						}
					}
				}
				// esquina superior izq
			} else if (x - d < 0 && x + d < m && y - d < 0 && y + d < m) {
				for (int xm = -d; xm <= d; xm++) {
					for (int ym = -d; ym <= d; ym++) {

						if (x + xm < 0 && y + ym < 0) {
							cell = matrix.get(m + x + xm, m + y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x +l, particle.y+l, radius, result);
						} else if (x + xm < 0) {
							cell = matrix.get(m + x + xm, y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x +l, particle.y, radius, result);
						} else if (y + ym < 0) {
							cell = matrix.get(x + xm, m + y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x, particle.y+l, radius, result);
						} else {
							cell = matrix.get(x + xm, y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x, particle.y, radius, result);
						}
					}
				}
				// esquina superior der
			} else if (x - d >= 0 && x + d >= m && y - d < 0 && y + d < m) {
				for (int xm = -d; xm <= d; xm++) {
					for (int ym = -d; ym <= d; ym++) {

						if (x + xm >= m && y + ym < 0) {
							cell = matrix.get(x + xm - m, m + y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x -l, particle.y+l, radius, result);
						} else if (x + xm >= m) {
							cell = matrix.get(x + xm - m, y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x -l, particle.y, radius, result);
						} else if (y + ym < 0) {
							cell = matrix.get(x + xm, m + y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x, particle.y+l, radius, result);
						} else {
							cell = matrix.get(x + xm, y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x, particle.y, radius, result);
						}
					}
				}
				// esquina inf der
			} else if (x - d >= 0 && x + d >= m && y - d >= 0 && y + d >= m) {
				for (int xm = -d; xm <= d; xm++) {
					for (int ym = -d; ym <= d; ym++) {

						if (x + xm >= m && y + ym >= m) {
							cell = matrix.get(x + xm - m, y + ym - m);
							addNeighborsOfSideCell(cell, particle, particle.x -l, particle.y-l, radius, result);
						} else if (x + xm >= m) {
							cell = matrix.get(x + xm - m, y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x -l, particle.y, radius, result);
						} else if (y + ym >= m) {
							cell = matrix.get(x + xm, y + ym - m);
							addNeighborsOfSideCell(cell, particle, particle.x, particle.y-l, radius, result);
						} else {
							cell = matrix.get(x + xm, y + ym);
							addNeighborsOfSideCell(cell, particle, particle.x, particle.y, radius, result);
						}
					}
				}

//			 esquina inf izq
		} else if (x - d < 0 && x + d < m && y - d >= 0 && y + d >= m) {
			for (int xm = -d; xm <= d; xm++) {
				for (int ym = -d; ym <= d; ym++) {
					
					if (x + xm < 0 && y + ym >= m) {
						cell = matrix.get(m + x + xm , y + ym - m);	
						addNeighborsOfSideCell(cell, particle, particle.x +l, particle.y-l, radius, result);
					} else if (x + xm < 0) {
						cell = matrix.get(m + x + xm, y + ym);
						addNeighborsOfSideCell(cell, particle, particle.x +l, particle.y, radius, result);
					} else if (y + ym >= m) {
						cell = matrix.get(x + xm, y + ym - m);
						addNeighborsOfSideCell(cell, particle, particle.x, particle.y-l, radius, result);
					} else {
						cell = matrix.get(x + xm, y + ym);
						addNeighborsOfSideCell(cell, particle, particle.x, particle.y, radius, result);
					}
				}
			}
		}
			
		}
	}

	private void addNeighborsOfSideCell(List<Particle> cell, Particle particle, double selX, double selY , double radius, Neighbors result) {
		for (Particle neighbor : cell) {
			double dist = distanceCPC(particle, neighbor, selX, selY );
			if (particle != neighbor && dist <= radius) {
				debug("Neighbor (%f; %f) is within radius (distance: %f)", neighbor.x, neighbor.y, dist);
				result.addNeighbors(particle, neighbor);
			}
		}
	}
	
	static double distanceCPC(Particle selected, Particle particle, double selX, double selY ) {
        double dx = Math.abs(particle.x - selX);
        double dy = Math.abs(particle.y - selY);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return Math.max(0, distance - particle.radius - selected.radius);
    }
	

	private Matrix<List<Particle>> getMatrixFromParticles(List<Particle> particles) {
		Matrix<List<Particle>> matrix = new Matrix<>(m, m);

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				matrix.put(i, j, new ArrayList<>());
			}
		}

		for (Particle p : particles) {
			int x = (int) (p.x / (l / m));
			int y = (int) (p.y / (l / m));

			List<Particle> cell = matrix.get(x, y);
			cell.add(p);
		}

		return matrix;
	}
}
