package ar.edu.itba.simul.group8.tp2;

import ar.edu.itba.simul.group8.common.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class OffLattice {

	private double modVelocity;
	private double noise;
	private Random rand;
	private Map<Particle, Double> birdMap;

	public OffLattice(List<Particle> particles, double modVelocity, double noise, Random rand) {
		this.modVelocity = modVelocity;
		this.noise = noise * 180 / Math.PI;
		this.rand = rand;

		initializeVelocities(particles);
	}

	private void initializeVelocities(List<Particle> particles) {
		birdMap = new HashMap<Particle, Double>();
		for (Particle p : particles) {
			birdMap.put(p, Math.toRadians(rand.nextDouble() * (360)));
		}
	}

	public void calcularVelocidades(Map<Particle, Set<Particle>> allNeighbors) {
		HashMap<Particle, Double> bm = new HashMap<Particle, Double>();

		for (Particle p : birdMap.keySet()) {
			double degressNoise = rand.nextDouble() * (noise) - noise / 2.0;
			
			Set<Particle> neighbors = allNeighbors.get(p);
			double sumSin = 0;
			double sumCos = 0;
			sumSin += Math.sin(birdMap.get(p));
			sumCos += Math.cos(birdMap.get(p));
			if (neighbors != null) {
				for (Particle n : neighbors) {
					sumSin += Math.sin(birdMap.get(n));
					sumCos += Math.cos(birdMap.get(n));
				}
			
				double radians = Math.atan(((double) sumSin / (neighbors.size()+1)) / ((double) sumCos / (neighbors.size()+1)));
				// double degressNoise=0;
				bm.put(p, Math.toRadians(Math.toDegrees(radians) + degressNoise));
			} else {
				double 	radians = Math.atan( ((double) sumSin) / ((double) sumCos));
				
				bm.put(p, Math.toRadians(Math.toDegrees(radians) + degressNoise));
			}
			
//			double rad = birdMap.get(p);
//			p.avg = p.id;
//			if (neighbors != null) {
////				System.out.println("R: " + rad);
//				for (Particle n : neighbors) {
//					rad += birdMap.get(n);
//					p.avg += n.id;
//				}
////				}
//				rad = rad / (neighbors.size() + 1);
////				System.out.println("R-: " + rad);
//				p.avg = p.avg / (neighbors.size() + 1);
//			}
			
//			bm.put(p, rad);
			// System.out.println(radians);
		}
		birdMap = bm;
	}

	public List<Particle> reposicionarParticulas(double l, long time) {
		List<Particle> particles = new ArrayList<Particle>();
		for (Particle p : birdMap.keySet()) {
			
			double x = repositionAxis(p.x + modVelocity * Math.cos(birdMap.get(p)) * time, l);
			double y = repositionAxis(p.y + modVelocity * Math.sin(birdMap.get(p)) * time, l);

			//System.out.println(p.x + "  "+ p.y+ "    "+x+", "+y + " l: " + l);
			//System.out.flush();
			
//			va.add(new Point(modVelocity * Math.cos(birdMap.get(p)), modVelocity * Math.sin(birdMap.get(p))));
			p.x = x;
			p.y = y;
			
			
			particles.add(p);
		}
		return particles;
	}

	public double getVa() {
		double x = 0;
		double y = 0;
		
		for (Particle p : birdMap.keySet()) {
			
			x += modVelocity * Math.cos(birdMap.get(p));
			y += modVelocity * Math.sin(birdMap.get(p));
		}
		return Math.sqrt(x*x+y*y)/(modVelocity*birdMap.size());
	}
	
	// If particle is out of limits, relocate it.
	private double repositionAxis(double d, double l) {
		while (d < 0)
			d += l;
		while (d >= l)
			d -= l;
		return d;
	}

	
	private class Point {
		double x,y;
		public Point(double x, double y) {
			this.x=x;
			this.y=y;
		}
	}
}
