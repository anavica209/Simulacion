package ar.edu.itba.simul.group8;

public class Particle {
  public final double x;
	public final double y;
	public final double radius;
	
	public Particle(double x, double y, double radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

    @Override
    public String toString() {
        return "Particle{" +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                '}';
    }
}
