package ar.edu.itba.simul.group8.tp1;

import ar.edu.itba.simul.group8.common.Particle;

import java.util.List;

public class BruteForceSearch extends NeighborSearch {

    public BruteForceSearch(List<Particle> particles, double l, int m) {
        super(particles, l, m);
    }

    @Override
    public void search(double radius, Neighbors result) {
        for (Particle p1 : particles) {
            for (Particle p2 : particles) {
                if (p1 != p2) {

                    debug("Comparing (%f; %f) with (%f; %f)", p1.x, p1.y, p2.x, p2.y);

                    double dist = distance(p1, p2);

                    if (dist <= radius) {
                        debug("Distance is %f, so they are neighbors", dist);

                        result.addNeighbors(p1, p2);
                    }
                }
            }
        }
    }
}
