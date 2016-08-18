package ar.edu.itba.simul.group8;

public class Particle {

    public final int id;
    public final double x;
    public final double y;
    public final double radius;

    public Particle(int id, double x, double y, double radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "P(" + id + ")";
    }
}
