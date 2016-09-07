package ar.edu.itba.simul.group8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Browniano {

	private Double tiempoImpacto;
	private Particle particleImpact1;
	private Particle particleImpact2;
	private double square;

	public Browniano(List<Particle> particles, double brwVelocity, Random rand, double square) {
		Particle bigp = null;
		for (Particle p : particles) {
			p.vx = rand.nextDouble() * 2 * brwVelocity - brwVelocity;
			p.vy = rand.nextDouble() * 2 * brwVelocity - brwVelocity;
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

				double deltaVR = deltaVX * deltaX + deltaVY * deltaY;
				double deltaVV = deltaVX * deltaVX + deltaVY * deltaVY;

				double deltaRR = deltaX * deltaX + deltaY * deltaY;

				double d = deltaVR - deltaVV * (deltaRR - sigma * sigma);

				if (!(d >= 0 || deltaVR < 0)) {
					hayColision = true;

					double tc = -((deltaVR + Math.sqrt(d)) / (deltaVV * deltaVV));
					if (timeImpact == 0 || timeImpact > tc) {
						timeImpact = tc;
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
				hayColision = true;
				if (p.vx > 0) {
					tc = (square - p.x) / p.vx;
				} else {
					tc = (square - p.x) / p.vx;
				}
			} else if (p.vx == 0 && p.vy != 0) {
				hayColision = true;
				if (p.vy > 0) {
					tc = (square - p.y) / p.vy;
				} else {
					tc = (square - p.y) / p.vy;
				}
			} else {
				hayColision = true;
				if (p.vx > 0) {
					tc = (square - p.x) / p.vx;
				} else {
					tc = (square - p.x) / p.vx;
				}
				double tc2;
				if (p.vy > 0) {
					tc2 = (square - p.y) / p.vy;
				} else {
					tc2 = (square - p.y) / p.vy;
				}
				tc = Math.min(tc, tc2);

			}
			if ((timeImpact == 0 || timeImpact > tc) && tc != 0) {
				timeImpact = tc;
				particleImpact1 = p;
				particleImpact2 = null;
			}
		}

		if (!hayColision)
			return null;
		return tiempoImpacto = timeImpact;
	}

	public List<Map<String, Object>> evolucionarSistema(List<Particle> ppp) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (Particle p : ppp) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("particle", p);
			m.put("x1", p.x);
			m.put("y1", p.y);
			double x = p.x + p.vx * tiempoImpacto;
			double y = p.y + p.vy * tiempoImpacto;

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
			double sigma = particleImpact2.radius + particleImpact1.radius;
			double deltaX = particleImpact2.x - particleImpact1.x;
			double deltaY = particleImpact2.y - particleImpact1.y;

			double deltaVX = particleImpact2.vx - particleImpact1.vx;
			double deltaVY = particleImpact2.vy - particleImpact1.vy;

			double deltaVR = deltaVX * deltaX + deltaVY * deltaY;

			double jota = (2 * particleImpact1.mass * particleImpact2.mass * deltaVR)
					/ (sigma * (particleImpact1.mass + particleImpact2.mass));

			particleImpact1.vx = particleImpact1.vx + (jota * deltaX / sigma) * particleImpact1.mass;
			particleImpact1.vy = particleImpact1.vy + (jota * deltaY / sigma) * particleImpact1.mass;

			particleImpact2.vx = particleImpact2.vx - (jota * deltaX / sigma) * particleImpact2.mass;
			particleImpact2.vy = particleImpact2.vy - (jota * deltaY / sigma) * particleImpact2.mass;
			
		} else {
			Particle p = particleImpact1;
			if (p.vx != 0 && p.vy == 0) {
				particleImpact1.vx = -particleImpact1.vx;
			} else if (p.vx == 0 && p.vy != 0) {
				particleImpact1.vy = -particleImpact1.vy;
			} else {
				double tc;
				if (p.vx > 0) {
					tc = (square - p.x) / p.vx;
				} else {
					tc = (square - p.x) / p.vx;
				}
				if (tc == tiempoImpacto) {
					particleImpact1.vx = -particleImpact1.vx;
				} else {
					particleImpact1.vy = -particleImpact1.vy;
				}
			}
		}
	}

	public Double getTiempoImpacto() {
		return tiempoImpacto;
	}

}
