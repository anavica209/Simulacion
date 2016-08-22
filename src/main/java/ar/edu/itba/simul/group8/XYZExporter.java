package ar.edu.itba.simul.group8;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

public class XYZExporter {

    private final String filename;

    XYZExporter(String filename) {
        this.filename = filename;
    }

    void export(List<Particle> particles) throws IOException {
        Writer w = new BufferedWriter(new FileWriter(filename));

        w.write(String.format("%d\n", particles.size()));
        w.write("Particles (x, y, radius, r, g, b)\n");

        for (Particle p : particles) {
            writeParticle(w, p);
        }

        w.close();
    }

    void exportWithSelection(List<Particle> particles, Particle selection, Set<Particle> neighbors) throws IOException {
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
}
