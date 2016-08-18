package ar.edu.itba.simul.group8;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {

    public static void main(String[] args) throws IOException {
        Random rand = new Random();
        int numParticles = 10;
        double l = 100;
        int m = 10;

        List<Particle> particles = generateParticles(numParticles, l);

        NeighborSearch search = new BruteForceSearch(particles, l, m);

        int random = rand.nextInt(particles.size());
        Neighbors neighbors = search.timedSearch(10.0);

        System.out.println("Neighbors: " + neighbors.getAllNeighbors().toString());
        System.out.println("Execution time: " + neighbors.getExecutionTime());

        XYZExporter exporter = new XYZExporter(Paths.get("./data/particles.xyz").toString());
        exporter.export(particles);
    }

    public static List<Particle> generateParticles(int numParticles, double l) {
        Random rand = new Random();

        List<Particle> particles = new ArrayList<Particle>(numParticles);

        for (int i = 0; i < numParticles; i++) {
            // TODO: make sure we never get the upper boundary
            double x = rand.nextDouble() * l;
            double y = rand.nextDouble() * l;
            double radius = rand.nextDouble() * 10;
            particles.add(new Particle(x, y, radius));
        }

        return particles;
    }
}
