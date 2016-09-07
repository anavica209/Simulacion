package ar.edu.itba.simul.group8;

public class Particle {

    public final int id;
    public double x;
    public double y;
    public double radius;
    public double avg;
	public double mass;
	public double vx;
	public double vy;

    public Particle(int id, double x, double y, double radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Particle(int i, double x2, double y2, Double bigR, Double bigMass) {
		this(i, x2, y2, bigR);
		this.mass=bigMass;
	}

	@Override
    public String toString() {
        return "P(" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Particle particle = (Particle) o;

        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
