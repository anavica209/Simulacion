package ar.edu.itba.simul;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class CellIndexSearchTests {

    private static final double SIZE = 100.0;
    private static final double MAX_RADIUS = 10.0;

    private List<Particle> generateParticles(int num) {
        List<Particle> particles = new ArrayList<>(num);

        Random rand = new Random();

        for (int i = 0; i < num; i++) {
            double x = rand.nextDouble() * SIZE;
            double y = rand.nextDouble() * SIZE;
            double radius = rand.nextDouble() * MAX_RADIUS;
            particles.add(new Particle(x, y, radius));
        }

        return particles;
    }

    @Test
    public void equivalentToBruteSearch() {
        List<Particle> particles = generateParticles(10);
        NeighborSearch callIndex = new CellIndexSearch(particles, SIZE, 10);
        NeighborSearch bruteForce = new BruteForceSearch(particles, SIZE, 10);

        Neighbors neighbors = callIndex.timedSearch(10.0);
        Neighbors expected = bruteForce.timedSearch(10.0);

        assertEquals(expected, neighbors);
    }
}
