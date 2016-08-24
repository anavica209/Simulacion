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

	private Double rmin=Math.toRadians(-180);
	private Double rmax=Math.toRadians(180);
	
	public OffLattice(List<Particle> particles, double modVelocity, double noise, Random rand) {
		this.modVelocity=modVelocity;
		this.noise=noise;
		this.rand=rand;
		
		initializeVelocities(particles);
	}

	private void initializeVelocities(List<Particle> particles) {
		birdMap=new HashMap<Particle, Double>(); 
		for(Particle p: particles){
			birdMap.put(p, rand.nextDouble()*(rmax-rmin));
		}
	}

	public void calcularVelocidades(Map<Particle, Set<Particle>> allNeighbors) {
		HashMap<Particle, Double> bm = new HashMap<Particle, Double>(); 
		
		for(Particle p: birdMap.keySet()){
			Set<Particle> neighbors=allNeighbors.get(p);
			double sumSin=0;
			double sumCos=0;
			for(Particle n: neighbors){
				sumSin+=Math.sin(birdMap.get(n));
				sumCos+=Math.cos(birdMap.get(n));
			}
			double radians=Math.atan(((double)sumSin/neighbors.size())/ ((double)sumCos/neighbors.size()));
			bm.put(p, radians+rand.nextDouble()*(noise)-noise/2);
		}
		birdMap=bm;
	}

	public List<Particle> reposicionarParticulas() {
		List<Particle> particles=new ArrayList<Particle>();
		for(Particle p: birdMap.keySet()){
			double x = p.x+modVelocity*Math.cos(birdMap.get(p));
			double y = p.y+modVelocity*Math.sin(birdMap.get(p));
			particles.add(new Particle(p.id, x, y, p.radius));
		}
		return particles;
	}
	

}
