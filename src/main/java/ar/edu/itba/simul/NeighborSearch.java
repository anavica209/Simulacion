package ar.edu.itba.simul;

import java.util.Collections;
import java.util.List;

public abstract class NeighborSearch {

    protected final List<Particle> particles;
    protected final double n;
    protected final double l;
    protected final double m;

    NeighborSearch(List<Particle> particles, double n, double l, double m) {
        this.particles = particles;
        this.n = n;
        this.l = l;
        this.m = m;
    }

    public Neighbors timedSearch(Particle selected, double radius) {
        long start = System.nanoTime();
        List<Particle> result = search(selected, radius);
        long end = System.nanoTime();

        return new Neighbors(result, end - start);
    }

    public abstract List<Particle> search(Particle selected, double radius);

    static class Neighbors {
        private final List<Particle> particles;
        private final long executionTime;

        Neighbors(List<Particle> particles, long executionTime) {
            this.particles = particles;
            this.executionTime = executionTime;
        }

        public List<Particle> getParticles() {
            return Collections.unmodifiableList(particles);
        }

        public long getExecutionTime() {
            return executionTime;
        }
    }
}
