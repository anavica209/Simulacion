package ar.edu.itba.simul.group8.tp2;

import ar.edu.itba.simul.group8.common.Particle;
import ar.edu.itba.simul.group8.tp1.*;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static final double MAX_RADIUS = 2.0;

    private static final int BRUTE_FORCE = 0;
    private static final int CIM = 1;
    private static final int CIMCPC = 2;

    private static final int OLNOISE = 1;
    private static final int OLDENSITY = 2;
    private static final int OLNONE = 0;

    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();

        OptionSpec<Integer> nOpt = parser.accepts("n").withRequiredArg().ofType(Integer.class).defaultsTo(20);
        OptionSpec<Integer> lOpt = parser.accepts("l").withRequiredArg().ofType(Integer.class).defaultsTo(100);
        OptionSpec<Integer> mOpt = parser.accepts("m").withRequiredArg().ofType(Integer.class).defaultsTo(10);
        OptionSpec<Integer> searchOpt = parser.accepts("search").withRequiredArg().ofType(Integer.class)
                .defaultsTo(CIM);

        OptionSpec<Integer> optionOpt = parser.accepts("option").withRequiredArg().ofType(Integer.class).defaultsTo(OLNONE);

        OptionSpec<Double> modVelocityOpt = parser.accepts("modVelocity").withRequiredArg().ofType(Double.class).defaultsTo(1.0);
        OptionSpec<Long> timeOpt = parser.accepts("time").withRequiredArg().ofType(Long.class).defaultsTo(500L);
        OptionSpec<Double> noiseOpt = parser.accepts("noise").withRequiredArg().ofType(Double.class).defaultsTo(0.0);
        OptionSpec<Integer> nroIteracionesOpt = parser.accepts("nroIteraciones").withRequiredArg().ofType(Integer.class).defaultsTo(2);


        OptionSet options = null;
        try {
            options = parser.parse(args);
        } catch (OptionException e) {
            System.err.println("error: " + e.getMessage());
            System.exit(1);
        }

        Random rand = new Random();
        rand.setSeed(1337);

        int numParticles = options.valueOf(nOpt);
        int l = options.valueOf(lOpt);
        int m = options.valueOf(mOpt);
        int searchType = options.valueOf(searchOpt);
        int option= options.valueOf(optionOpt);
        int nroIteraciones=options.valueOf(nroIteracionesOpt);

        double modVelocity = options.valueOf(modVelocityOpt);
        long time = options.valueOf(timeOpt);
        double noise = options.valueOf(noiseOpt);

        offLattice(option, numParticles, l, m, time, modVelocity, noise, rand, searchType, nroIteraciones);
    }

    private static void offLattice(int option, int numParticles, int l, int m, long time, double modVelocity, double noise, Random rand, int searchType, int nroIteraciones) throws IOException {

        if(option==OLNOISE){
            XYZExporter exporter = new XYZExporter(Paths.get("./data/va-noise.csv").toString());
            Writer writer=exporter.startWriter();

            for(double r=0.0; r<=5.0; r+=0.2){
                //promedio de varias corridas
                double va=0;
                for(int iteration=0; iteration<nroIteraciones; iteration++){
                    List<Particle> particles = generateParticles(numParticles, l, 0.0);

                    OffLattice offLatticeImpl= new OffLattice(particles, modVelocity, r, rand);

                    for(long t=0; t<time; t++){
                        System.out.println("r: "+ r +" iteracion: " + iteration +" t: " + (time - t));
                        Neighbors neighbors = runAlgorithm(particles, searchType, numParticles, l, m, 1);
                        offLatticeImpl.calcularVelocidades(neighbors.getAllNeighbors());
                        particles=offLatticeImpl.reposicionarParticulas(l, 1);
                    }
                    va+=offLatticeImpl.getVa();
                }
                va=va/nroIteraciones;
                exporter.addCVSLine(writer, va, r);
            }
            writer.close();


        }else if(option==OLDENSITY){

            XYZExporter exporter = new XYZExporter(Paths.get("./data/va-density.csv").toString());
            Writer writer=exporter.startWriter();

            for(int nParticles=1; nParticles<=4001; nParticles+=1000){
                //promedio de varias corridas
                double va=0;
                for(int iteration=0; iteration<nroIteraciones; iteration++){
                    List<Particle> particles = generateParticles(nParticles, l, 0.0);

                    OffLattice offLatticeImpl= new OffLattice(particles, modVelocity, noise, rand);

                    for(long t=0; t<time; t++){
                        System.out.println("n: "+ nParticles +" iteracion: " + iteration +" t: " + (time - t));
                        Neighbors neighbors = runAlgorithm(particles, searchType, nParticles, l, m, 1);
                        offLatticeImpl.calcularVelocidades(neighbors.getAllNeighbors());
                        particles=offLatticeImpl.reposicionarParticulas(l, 1);
                    }
                    va+=offLatticeImpl.getVa();
                }
                va=va/nroIteraciones;
                exporter.addCVSLine(writer, va, ((double)nParticles)/l*l);
            }
            writer.close();




        }else{
            List<Particle> particles = generateParticles(numParticles, l, 0.0);

            XYZExporter exporter = new XYZExporter(Paths.get("./data/particles.xyz").toString());
            OffLattice offLatticeImpl= new OffLattice(particles, modVelocity, noise, rand);

            Writer writer=exporter.startWriter();
//			System.out.println("Va:" + offLatticeImpl.getVa());
            for(long t=0; t<time; t++){
                System.out.println("t: " + (time - t));
                Neighbors neighbors = runAlgorithm(particles, searchType, numParticles, l, m, l/m);
                offLatticeImpl.calcularVelocidades(neighbors.getAllNeighbors());
                particles=offLatticeImpl.reposicionarParticulas(l, 1);

                exporter.exportOffLattice(writer, particles, t);
            }
            writer.close();

//			System.out.println("Va-:" + offLatticeImpl.getVa());
        }


    }

    public static Neighbors runAlgorithm(List<Particle> particles, int searchType, int numParticles, double lindex, int m, double radious) {
        NeighborSearch search;
        switch (searchType) {
            case BRUTE_FORCE:
                search = new BruteForceSearch(particles, lindex, m);
                break;
            case CIM:
                search = new CellIndexSearch(particles, lindex, m);
            default:
                search = new CellIndexSearchCPC(particles, lindex, m);
        }

        return search.timedSearch(radious);
    }

    public static List<Particle> generateParticles(int numParticles, double l, Double fixedRadius) {
        Random rand = new Random();

        List<Particle> particles = new ArrayList<Particle>(numParticles);

        for (int i = 0; i < numParticles; i++) {
            // TODO: make sure we never get the upper boundary
            double x = rand.nextDouble() * l-0.001;
            double y = rand.nextDouble() * l-0.001;
            double radius = (fixedRadius != null) ? fixedRadius : rand.nextDouble() * MAX_RADIUS;
            particles.add(new Particle(i + 1, x, y, radius));
        }

        return particles;
    }
}
