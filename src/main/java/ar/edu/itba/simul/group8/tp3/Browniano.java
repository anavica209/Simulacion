package ar.edu.itba.simul.group8.tp3;

import ar.edu.itba.simul.group8.common.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Browniano {

	private Double tiempoImpacto;
	public Particle particleImpact1;
	public Particle particleImpact2;
	private double square;

	public Browniano(List<Particle> particles, double brwVelocity, Random rand,
			double square) {
		Particle bigp = null;
		for (Particle p : particles) {
			p.vx = rand.nextDouble() * 2 * brwVelocity - brwVelocity;
			p.vy = rand.nextDouble() * 2 * brwVelocity - brwVelocity;
//			double angle = rand.nextDouble() * 2 * Math.PI;
//			p.vy = Math.cos((angle));
//			p.vx = Math.sin((angle));
			if (bigp == null) {
				bigp = p;
			} else if (bigp.mass < p.mass) {
				bigp = p;
			}
		}
		bigp.vx = 0;
		bigp.vy = 0;

		this.square = square;
	}
	
	public Browniano(List<Particle> particles, double brwVelocity, Random rand,
			double square, double smallMass, double bigMass) {
		double bigV = Math.sqrt( (particles.size() - 1) * smallMass * brwVelocity * brwVelocity / bigMass);
		
		Particle bigp = null;
		for (Particle p : particles) {
			p.vx = 0;
			p.vy = 0;//rand.nextDouble() * 2 * brwVelocity - brwVelocity;
			if (bigp == null) {
				bigp = p;
			} else if (bigp.mass < p.mass) {
				bigp = p;
			}
		}
		bigp.vx = rand.nextDouble() * 2 * bigV - bigV;;
		bigp.vy = rand.nextDouble() * 2 * bigV - bigV;;

		this.square = square;
	}

	public Double calcularTiempoImpacto(List<Particle> particles) {
		tiempoImpacto = null;
		double timeImpact = 0;
		boolean hayColision = false;
		for (int i = 0; i < particles.size(); i++) {
			for (int j = i + 1; j < particles.size(); j++) {

				Particle pj = particles.get(j);
				Particle pi = particles.get(i);
				
				double sigma = pi.radius + pj.radius;
				double deltaX = pj.x - pi.x;
				double deltaY = pj.y - pi.y;

				double deltaVX = pj.vx - pi.vx;
				double deltaVY = pj.vy - pi.vy;
				
				double deltaRR = deltaX * deltaX + deltaY * deltaY;

				double deltaVV = deltaVX * deltaVX + deltaVY * deltaVY;
				
				double deltaVR = deltaVX * deltaX + deltaVY * deltaY;

				double d = deltaVR * deltaVR - deltaVV * (deltaRR - sigma * sigma);

				if (!(d < 0 || deltaVR >= 0)) {
					double tc = -((deltaVR + Math.sqrt(d)) / deltaVV);
					if ((timeImpact == 0 && tc>0)
							|| (tc > 0 && timeImpact > tc && timeImpact > 0)) {
						timeImpact = tc;
						hayColision = true;
						particleImpact1 = pj;
						particleImpact2 = pi;
					}
				}
			}
		}
		
		for (Particle p : particles) {
			double tc = 0.0;
			if (p.vx == 0 && p.vy == 0) {
				;
			} else if (p.vx != 0 && p.vy == 0) {
				if (p.vx > 0) {
					tc = (square - p.radius - p.x) / p.vx;
				} else {
					tc = (0 + p.radius - p.x) / p.vx;
				}
			} else if (p.vx == 0 && p.vy != 0) {
				if (p.vy > 0) {
					tc = (square - p.radius - p.y) / p.vy;
				} else {
					tc = (0 + p.radius - p.y) / p.vy;
				}
			} else {
				if (p.vx > 0) {
					tc = (square - p.radius - p.x) / p.vx;
				} else {
					tc = (0 + p.radius - p.x) / p.vx;
				}
				double tc2;
				if (p.vy > 0) {
					tc2 = (square - p.radius - p.y) / p.vy;
				} else {
					tc2 = (0 + p.radius - p.y) / p.vy;
				}
				if (tc < 0 && tc2 > 0) {
					tc = tc2;
				} else if (tc > 0 && tc2 > 0) {
					tc = Math.min(tc, tc2);
				}

			}
			if ((timeImpact == 0 && tc>0)
					|| (tc > 0 && timeImpact > tc && timeImpact > 0)) {
				hayColision = true;
				timeImpact = tc;
				particleImpact1 = p;
				particleImpact2 = null;
			}
		}

		if (!hayColision)
			return null;
		return tiempoImpacto = timeImpact;
	}

	public List<Map<String, Object>> evolucionarSistema(List<Particle> ppp, double time) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (Particle p : ppp) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("particle", p);
			m.put("x1", p.x);
			m.put("y1", p.y);
			double x = p.x + p.vx * time;
			double y = p.y + p.vy * time;

			p.x = x;
			p.y = y;
			
			m.put("x2", p.x);
			m.put("y2", p.y);
			list.add(m);

		}
		return list;
	}

	public void calcularVelocidades(List<Particle> particles) {
		if (particleImpact2 != null) {
//			if (particleImpact1.mass > 10.0 || 10.0 < particleImpact2.mass){
//	            System.out.println("\tp1 y:"+ particleImpact1.y+ " mass: "+particleImpact1.mass );
//	            System.out.println("\tp1 vx:"+ particleImpact1.vx+"\tp1 vy:"+ particleImpact1.vy);
//	            
//	            System.out.println("\tp2 x:"+ particleImpact2.x+"\tp2 y:"+ particleImpact2.y+ " mass: "+particleImpact2.mass );
//	            System.out.println("\tp2 vx:"+ particleImpact2.vx+"\tp2 vy:"+ particleImpact2.vy);
//	            System.out.println();
//			}
			double sigma = particleImpact2.radius + particleImpact1.radius;
			double deltaX = particleImpact2.x - particleImpact1.x;
			double deltaY = particleImpact2.y - particleImpact1.y;

			double deltaVX = particleImpact2.vx - particleImpact1.vx;
			double deltaVY = particleImpact2.vy - particleImpact1.vy;

			double deltaVR = deltaVX * deltaX + deltaVY * deltaY;

			double jota = (2 * particleImpact1.mass * particleImpact2.mass * deltaVR)
					/ (sigma * (particleImpact1.mass + particleImpact2.mass));

			particleImpact1.vx = particleImpact1.vx + (jota * deltaX / sigma) / particleImpact1.mass;
			particleImpact1.vy = particleImpact1.vy + (jota * deltaY / sigma) / particleImpact1.mass;

			particleImpact2.vx = particleImpact2.vx - (jota * deltaX / sigma) / particleImpact2.mass;
			particleImpact2.vy = particleImpact2.vy - (jota * deltaY / sigma) / particleImpact2.mass;

		} else {
			Particle p = particleImpact1;

			if (p.vx > 0 && p.x + p.radius >= square) {
				p.vx = -p.vx;
			}
			else if (p.vx < 0 && p.x - p.radius <= 0) {
				p.vx = -p.vx;
			}
			
			if (p.vy > 0 && p.y + p.radius >= square) {
				p.vy = -p.vy;
			}
			else if (p.vy < 0 && p.y - p.radius <= 0) {
				p.vy = -p.vy;
			}
		}
	}

	public Double getTiempoImpacto() {
		return tiempoImpacto;
	}

}
