package simu1;

import java.awt.Point;

public class Particula {

	Point posicion;
	
	double radio;
	
	public Particula(Point posicion, double radio) {
		this.posicion=posicion;
		this.radio=radio;
	}

	public Point getPosicion() {
		return posicion;
	}

	public void setPosicion(Point posicion) {
		this.posicion = posicion;
	}

	public double getRadio() {
		return radio;
	}

	public void setRadio(double radio) {
		this.radio = radio;
	}
	
}
