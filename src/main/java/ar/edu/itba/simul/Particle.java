package ar.edu.itba.simul;

public class Particle {
  public final double x;
	public final double y;
	public final double radius;
	
	public Particle(double x, double y, double radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	public String toString() {
		return String.format("Particle{x=%.2f;y=%.2f;radius=%.2f}", x, y , radius);
	}
}
