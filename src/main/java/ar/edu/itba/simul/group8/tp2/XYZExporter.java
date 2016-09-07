package ar.edu.itba.simul.group8.tp2;

import ar.edu.itba.simul.group8.common.Particle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

public class XYZExporter {

    private final String filename;

    public XYZExporter(String filename) {
        this.filename = filename;
    }

    public void export(List<Particle> particles) throws IOException {
        Writer w = new BufferedWriter(new FileWriter(filename));

        w.write(String.format("%d\n", particles.size()));
        w.write("Particles (x, y, radius, r, g, b)\n");

        for (Particle p : particles) {
            writeParticle(w, p);
        }

        w.close();
    }

    public void exportWithSelection(List<Particle> particles, Particle selection, Set<Particle> neighbors) throws IOException {
        Writer w = new BufferedWriter(new FileWriter(filename));

        w.write(String.format("%d\n", particles.size()));
        w.write("Particles (x, y, radius, r, g, b)\n");

        for (Particle p : particles) {
            writeParticleWithSelection(w, p, selection, neighbors);
        }

        w.close();
    }

    void writeParticle(Writer writer, Particle particle) throws IOException {
        writer.write(String.format("%f\t%f\t%f\n", particle.x, particle.y, particle.radius));
    }

    void writeParticleWithSelection(Writer writer, Particle particle, Particle selection, Set<Particle> neighbors) throws IOException {
        if (particle == selection) {
            writer.write(String.format("%f\t%f\t%f\t%d\t%d\t%d\n", particle.x, particle.y, particle.radius, 0, 255, 0));
        } else if (neighbors.contains(particle)) {
            writer.write(String.format("%f\t%f\t%f\t%d\t%d\t%d\n", particle.x, particle.y, particle.radius, 0, 0, 255));
        } else {
            writer.write(String.format("%f\t%f\t%f\t%d\t%d\t%d\n", particle.x, particle.y, particle.radius, 255, 0, 0));
        }
    }

//    https://en.wikipedia.org/wiki/XYZ_file_format
	public void exportOffLattice(Writer writer, List<Particle> particles,  long t) throws IOException {
		writer.write(String.format("%d\n", particles.size()));
        writer.write("Particles (x, y, radius, r, g, b)  at "+t+"\n");
        
        for (Particle p : particles) {
        	writer.write(String.format("%f\t%f\t%f\t%d\t%d\t%d\n", p.x, p.y, 2.5, 255, (int) (p.id * 255.0 / 20), (int)(p.avg * 255.0 / 20)).replace(',', '.'));
        }
	}

	public Writer startWriter() throws IOException {
		 return new BufferedWriter(new FileWriter(filename));

	}

	public void addCVSLine(Writer writer, double param1, double param2) throws IOException {
		writer.write(String.format("%f, %f\n", param1, param2));
		
	}

	public void exportBrowniano(Writer writer, List<Particle> particles, long t) throws IOException {
		writer.write(String.format("%d\n", particles.size()));
        writer.write("Particles (x, y, x2, y2, radius, r, g, b)  at "+t+"\n");
        for (Particle p : particles) {
        	writer.write(String.format("%f\t %f\t %f\t %f\t %f\t %d\t%d\t%d\n", 
        			p.x, p.y, p.vx, p.vy,
        			p.radius, 255, 0, 0).replace(',', '.'));
        }
	}

	

}
