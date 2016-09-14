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
	
	public void addCVSLine(Writer writer, String param) throws IOException {
		writer.write(String.format("%s\n", param));
		
	}

	public void addCVSLineInt(Writer writer, int param1, double param2) throws IOException {
		writer.write(param1+", "+param2+"\n");
		
	}
	
	public void exportBrowniano(Writer writer, List<Particle> particles, long t, double square, Double smallR) throws IOException {
		writer.write(String.format("%d\n", particles.size()+4));
        writer.write("Particles (x, y, x2, y2, radius, r, g, b, selection)  at "+t+"\n");
        for (Particle p : particles) {
        	if (p.radius>10.00) {
        		writer.write(String.format("%f\t %f\t %f\t %f\t %f\t %d\t%d\t%d\t%d\n", 
            			p.x, p.y, p.vx, p.vy,
            			p.radius, 255, 0, 0,1).replace(',', '.'));
			} else if (Math.sqrt(Math.pow(p.vx, 2)+Math.pow(p.vy, 2)) > 0.09){
        	writer.write(String.format("%f\t %f\t %f\t %f\t %f\t %d\t%d\t%d\t%d\n", 
        			p.x, p.y, p.vx, p.vy,
        			p.radius, 255, 255, 0,0).replace(',', '.'));
        } else if (Math.sqrt(Math.pow(p.vx, 2)+Math.pow(p.vy, 2)) > 0.06){
        	writer.write(String.format("%f\t %f\t %f\t %f\t %f\t %d\t%d\t%d\t%d\n", 
        			p.x, p.y, p.vx, p.vy,
        			p.radius, 255, 255, 255,0).replace(',', '.'));
        } else {
        	writer.write(String.format("%f\t %f\t %f\t %f\t %f\t %d\t%d\t%d\t%d\n", 
        			p.x, p.y, p.vx, p.vy,
        			p.radius, 255, 0, 0,0).replace(',', '.'));
        }
        }
        
        writer.write(String.format("%f\t %f\t %f\t %f\t %f\t %d\t%d\t%d\t%d\n", 
    			0.0, 0.0, 0.0, 0.0,
    			smallR, 0, 255, 0, 0).replace(',', '.'));
        writer.write(String.format("%f\t %f\t %f\t %f\t %f\t %d\t%d\t%d\t%d\n", 
    			0.0, square, 0.0, 0.0,
    			smallR, 0, 255, 0, 0).replace(',', '.'));
        writer.write(String.format("%f\t %f\t %f\t %f\t %f\t %d\t%d\t%d\t%d\n", 
    			square, 0.0, 0.0, 0.0,
    			smallR, 0, 255, 0, 0).replace(',', '.'));
        writer.write(String.format("%f\t %f\t %f\t %f\t %f\t %d\t%d\t%d\t%d\n", 
    			square, square, 0.0, 0.0,
    			smallR, 0, 255, 0, 0).replace(',', '.'));
	}

	

}
