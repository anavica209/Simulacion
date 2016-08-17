package ar.edu.itba.simul;

public class Particle {

	double radio;

	private double x;

	private double y;
	
	public Particle(double x, double y, double radio) {
		this.x=x;
		this.y=y;
		this.radio=radio;
	}

	public double getRadio() {
		return radio;
	}

	public void setRadio(double radio) {
		this.radio = radio;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
}
