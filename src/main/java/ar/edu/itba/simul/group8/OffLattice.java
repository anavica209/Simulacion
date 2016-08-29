package ar.edu.itba.simul.group8;

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
		this.noise = noise;
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

		double degressNoise = rand.nextDouble() * (noise) - noise / 2.0;
		for (Particle p : birdMap.keySet()) {
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
				double radians = Math.atan( ((double) sumSin) / ((double) sumCos));
				
				bm.put(p, Math.toRadians(Math.toDegrees(radians) + degressNoise));
			}
			// System.out.println(radians);
		}
		birdMap = bm;
	}

	public List<Particle> reposicionarParticulas(double l, long time) {
		List<Particle> particles = new ArrayList<Particle>();
		for (Particle p : birdMap.keySet()) {
			
			double x = repositionAxis(p.x + modVelocity * Math.cos(birdMap.get(p)) * time, l);
			double y = repositionAxis(p.y + modVelocity * Math.sin(birdMap.get(p)) * time, l);

			 System.out.println(p.x + "  "+ p.y+ "    "+x+", "+y);
			particles.add(new Particle(p.id, x, y, p.radius));
		}
		return particles;
	}

	// If particle is out of limits, relocate it.
	private double repositionAxis(double d, double l) {
		if (d < 0)
			return l - d;
		if (d > l)
			return d - l;
		return d;
	}

}
