package ar.edu.itba.simul.group8.tp2;

import ar.edu.itba.simul.group8.common.Particle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DefaultImporter {

    private final String filename;

    public DefaultImporter(String filename) {
        this.filename = filename;
    }

    public List<Particle> importParticles() throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(filename));

        int n = new Scanner(r.readLine()).nextInt();
        int l = new Scanner(r.readLine()).nextInt();

        List<Particle> result = new ArrayList<Particle>(n);

        for (int i = 0; i < n; i++) {
            Scanner scanner = new Scanner(r.readLine());

            double radius = scanner.nextDouble();
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();

            result.add(new Particle(i + 1, x, y, radius));
        }

        return result;
    }

}
