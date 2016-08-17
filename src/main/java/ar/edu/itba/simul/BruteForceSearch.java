package ar.edu.itba.simul;

import java.util.ArrayList;
import java.util.List;

public class BruteForceSearch extends NeighborSearch {

    BruteForceSearch(List<Particle> particles, double n, double l, double m) {
        super(particles, n, l, m);
    }

    private double distance(Particle selected, Particle particle) {
        double dx = Math.abs(particle.x - selected.x);
        double dy = Math.abs(particle.x - selected.x);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return Math.max(0, distance - particle.radius - selected.radius);
    }

    public List<Particle> search(Particle selected, double radius) {
        List<Particle> neighbors = new ArrayList<Particle>();

        for (Particle particle : particles) {
            double distance = distance(selected, particle);
            if (distance <= radius && particle != selected) {
                neighbors.add(particle);
            }
        }

        return neighbors;
    }
}
