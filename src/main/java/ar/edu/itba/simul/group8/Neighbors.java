package ar.edu.itba.simul.group8;

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

    public Set<Pair<Particle, Particle>> getNeighborPairs() {
        Set<Pair<Particle, Particle>> set = new HashSet<>();

        for (Map.Entry<Particle, Set<Particle>> entry : neighbors.entrySet()) {
            for (Particle particle : entry.getValue()) {
                set.add(new Pair<>(entry.getKey(), particle));
            }
        }

        return set;
    }

    public void addNeighbors(Particle p1, Particle p2) {
        addNeighbor(p1, p2);
        addNeighbor(p2, p1);
    }

    private void addNeighbor(Particle p1, Particle p2) {
        Set<Particle> set = neighbors.get(p1);
        if (set == null) {
            set = new HashSet<>();
            neighbors.put(p1, set);
        }
        set.add(p2);
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
