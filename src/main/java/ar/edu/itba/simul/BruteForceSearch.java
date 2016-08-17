package ar.edu.itba.simul;

import java.util.List;

public class BruteForceSearch extends NeighborSearch {

    BruteForceSearch(List<Particle> particles, double l, int m) {
        super(particles, l, m);
    }

    public void search(double radius, Neighbors result) {
        for (Particle p1 : particles) {
            for (Particle p2 : particles) {
                double distance = distance(p1, p2);
                if (p1 != p2 && distance <= radius) {
                    result.addNeighbors(p1, p2);
                }
            }
        }
    }
}
