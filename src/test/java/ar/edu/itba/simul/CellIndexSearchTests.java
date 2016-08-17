package ar.edu.itba.simul;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class CellIndexSearchTests {

    private List<Particle> generateParticles(int num) {
        List<Particle> particles = new ArrayList<>(num);

        Random rand = new Random();

        for (int i = 0; i < num; i++) {
            double x = rand.nextDouble();
            double y = rand.nextDouble();
            double radius = rand.nextDouble();
            particles.add(new Particle(x, y, radius));
        }

        return particles;
    }

    @Test
    public void equivalentToBruteSearch() {
        List<Particle> particles = generateParticles(10);
        NeighborSearch callIndex = new CellIndexSearch(particles, 100.0, 10);
        NeighborSearch bruteForce = new BruteForceSearch(particles, 100.0, 10);

        Neighbors neighbors = callIndex.timedSearch(10.0);
        Neighbors expected = bruteForce.timedSearch(10.0);

        assertEquals(expected, neighbors);
    }
}
