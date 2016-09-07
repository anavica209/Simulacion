package ar.edu.itba.simul.group8.tp3;

import ar.edu.itba.simul.group8.common.Particle;
import ar.edu.itba.simul.group8.tp1.*;
import ar.edu.itba.simul.group8.tp2.OffLattice;
import ar.edu.itba.simul.group8.tp2.XYZExporter;
import ar.edu.itba.simul.group8.tp3.Browniano;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();

        OptionSpec<Integer> nOpt = parser.accepts("n").withRequiredArg().ofType(Integer.class).defaultsTo(20);
        OptionSpec<Long> timeOpt = parser.accepts("time").withRequiredArg().ofType(Long.class).defaultsTo(500L);


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
        long time = options.valueOf(timeOpt);

        System.out.println("browniano");
        Double bigMass = 100.0;
        Double smallMass = 0.1;
        Double bigR = 0.005;
        Double smallR = 0.0005;
        Double brwVelocity = 0.1;
        double square = 0.5;
        browniano(numParticles, square, time, bigMass, smallMass, bigR, smallR, brwVelocity, rand);
    }

    private static void browniano(int numParticles, double square, long brownTime, Double bigMass,
                                  Double smallMass, Double bigR, Double smallR, Double brwVelocity, Random rand) throws IOException {
        List<Particle> particles = generateParticlesBrown(numParticles, square, bigMass, smallMass, bigR, smallR, rand);

        XYZExporter exporter = new XYZExporter(Paths.get("./data/particles.xyz").toString());
        Browniano brownianoImpl= new Browniano(particles, brwVelocity, rand, square);

        Writer writer=exporter.startWriter();
        for(long t=0; t<brownTime; t++){
            brownianoImpl.calcularTiempoImpacto(particles);
            if(brownianoImpl.getTiempoImpacto()==null){
//				finalizar
                break;
            }
            System.out.println("t: " + (brownTime - t)+"\tTiempo impacto:"+brownianoImpl.getTiempoImpacto()+ "\tp1:"+ brownianoImpl.particleImpact1+"\tp2:"+ brownianoImpl.particleImpact2);
            System.out.println("\tp1 x:"+ brownianoImpl.particleImpact1.x+"\tp2 y:"+ brownianoImpl.particleImpact1.y);

            List<Map<String, Object>> evolvedParticles=brownianoImpl.evolucionarSistema(particles);
            brownianoImpl.calcularVelocidades(particles);

            exporter.exportOffLattice(writer, particles, t);
//			exporter.exportBrowniano(writer, evolvedParticles, t);
        }
        writer.close();
    }

    private static List<Particle> generateParticlesBrown(int numParticles,
                                                         Double square, Double bigMass, Double smallMass, Double bigR, Double smallR, Random rand) {

        List<Particle> particles = new ArrayList<Particle>(numParticles);

        double x = rand.nextDouble() * square-0.001;
        double y = rand.nextDouble() * square-0.001;
        particles.add(new Particle(0, x, y, bigR, bigMass));


        for (int i = 0; i < numParticles-1; i++) {
            int j=i;
            while(j==i){
                double x2 = rand.nextDouble() * square-0.001;
                double y2 = rand.nextDouble() * square-0.001;

                boolean isOver=false;
                for(Particle p: particles){
                    if(!((Math.pow(p.x-x2, 2)+Math.pow(p.y-y2, 2))> Math.pow(p.radius+smallR, 2))){
                        isOver=true;
                        break;
                    }
                }

                if(!isOver){
                    particles.add(new Particle(i + 1, x2, y2, smallR, smallMass));
                    j++;
                }
            }
        }

        return particles;
    }
}
