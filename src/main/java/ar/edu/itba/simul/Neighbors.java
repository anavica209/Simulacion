package ar.edu.itba.simul;

import java.util.*;

public class Neighbors {
    private final Map<Particle, Set<Particle>> neighbors = new HashMap<>();
    private long executionTime;

    Neighbors() {
    }

    public Set<Particle> getNeighbors(Particle particle) {
        return Collections.unmodifiableSet(neighbors.get(particle));
    }

    public Map<Particle, Set<Particle>> getAllNeighbors() {
        return neighbors;
    }

    public void addNeighbors(Particle p1, Particle p2) {
        addNeighbor(p1, p2);
        addNeighbor(p2, p1);
    }

    private void addNeighbor(Particle p1, Particle p2) {
        Set<Particle> list = neighbors.get(p1);
        if (list == null) {
            list = new HashSet<>();
            neighbors.put(p1, list);
        }
        list.add(p2);
    }

    void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
   }
    public long getExecutionTime() {
        return executionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Neighbors neighbors1 = (Neighbors) o;

        return neighbors.equals(neighbors1.neighbors);
    }

    @Override
    public int hashCode() {
        return neighbors.hashCode();
    }
}
