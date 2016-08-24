package ar.edu.itba.simul.group8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CellIndexSearchTest {

    private static final double SIZE = 100.0;
    private static final double MAX_RADIUS = 2.0;

    private List<Particle> generateParticles(int num) {
        List<Particle> particles = new ArrayList<Particle>(num);

        Random rand = new Random();

        for (int i = 0; i < num; i++) {
            double x = rand.nextDouble() * SIZE;
            double y = rand.nextDouble() * SIZE;
            double radius = rand.nextDouble() * MAX_RADIUS;
            particles.add(new Particle(i + 1, x, y, radius));
        }

        return particles;
    }

    @Test
    public void equivalentToBruteSearch() {
        List<Particle> particles = generateParticles(1000);
        NeighborSearch callIndex = new CellIndexSearch(particles, SIZE, 10);
        NeighborSearch bruteForce = new BruteForceSearch(particles, SIZE, 10);

        Neighbors n1 = callIndex.timedSearch(5);
        Neighbors n2 = bruteForce.timedSearch(5);

        Set<Pair<Particle, Particle>> expected = n1.getNeighborPairs();
        Set<Pair<Particle, Particle>> neighbors = n2.getNeighborPairs();

        assertEquals(expected, neighbors);

        System.out.println(n1.getExecutionTime());
        System.out.println(n2.getExecutionTime());
        assertTrue("Execution time is lower than brute force", n1.getExecutionTime() < n2.getExecutionTime());
    }
}
