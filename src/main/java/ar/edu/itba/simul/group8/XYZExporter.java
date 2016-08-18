package ar.edu.itba.simul.group8;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class XYZExporter {

    private final String filename;

    XYZExporter(String filename) {
        this.filename = filename;
    }

    void export(List<Particle> particles) throws IOException {
        Writer w = new BufferedWriter(new FileWriter(filename));

        w.write(String.format("%d\n", particles.size()));
        w.write("Particles\n");

        for (Particle p : particles) {
            writeParticle(w, p);
        }

        w.close();
    }

    void writeParticle(Writer writer, Particle particle) throws IOException {
        writer.write(String.format("%f\t%f\t%f\n", particle.x, particle.y, particle.radius));
    }

}
