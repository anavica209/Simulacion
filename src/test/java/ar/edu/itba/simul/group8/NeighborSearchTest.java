package ar.edu.itba.simul.group8;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NeighborSearchTest {

    @Test
    public void distance() {
        Particle p1 = new Particle(1, 0, 0, 0);
        Particle p2 = new Particle(2, 1, 1, 0);

        assertEquals(Math.sqrt(2), NeighborSearch.distance(p1, p2), 1e-10);
    }

}
