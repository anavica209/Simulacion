package ar.edu.itba.simul.group8.tp1;

import ar.edu.itba.simul.group8.common.Particle;
import ar.edu.itba.simul.group8.tp1.Neighbors;

import java.util.*;

public abstract class NeighborSearch {

    private static final boolean DEBUG = false;

    protected final List<Particle> particles;
    protected final double l;
    protected final int m;

    NeighborSearch(List<Particle> particles, double l, int m) {
        this.particles = particles;
        this.l = l;
        this.m = m;
    }

    protected void debug(String fmt, Object... args) {
        if (DEBUG) {
            System.out.println(String.format(fmt, args));
        }
    }

    static double distance(Particle selected, Particle particle) {
        double dx = Math.abs(particle.x - selected.x);
        double dy = Math.abs(particle.y - selected.y);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return Math.max(0, distance - particle.radius - selected.radius);
    }

    public Neighbors timedSearch(double radius) {
        init();

        long start = System.nanoTime();

        Neighbors result = new Neighbors();
        search(radius, result);

        long end = System.nanoTime();

        result.setExecutionTime(end - start);

        return result;
    }

    public void init() {
    }

    public abstract void search(double radius, Neighbors result);
}
