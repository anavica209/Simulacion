package simu1;

public class Particula {

	double radio;

	private double x;

	private double y;
	
	public Particula(double x, double y, double radio) {
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
