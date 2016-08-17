package ar.edu.itba.simul;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {

    public static void main( String[] args ) {
        Random rand = new Random();
        int numParticles = 10;

        List<Particle> particles = new ArrayList<Particle>(numParticles);

        for (int i = 0; i < numParticles; i++) {
            double x = rand.nextDouble() * 100;
            double y = rand.nextDouble() * 100;
            double radius = rand.nextDouble() * 10;
            particles.add(new Particle(x, y, radius));
        }

        NeighborSearch search = new BruteForceSearch(particles, 0, 0, 0);

        int random = rand.nextInt(particles.size());

        NeighborSearch.Neighbors neighbors = search.timedSearch(particles.get(random), 10.0);

        System.out.println("Neighbors: " + neighbors.getParticles().toString());
        System.out.println("Execution time: " + neighbors.getExecutionTime());
    }
}
