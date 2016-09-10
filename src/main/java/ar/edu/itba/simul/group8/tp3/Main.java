package ar.edu.itba.simul.group8.tp3;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import ar.edu.itba.simul.group8.common.Particle;
import ar.edu.itba.simul.group8.tp2.XYZExporter;

public class Main {

    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();

        OptionSpec<Integer> nOpt = parser.accepts("n").withRequiredArg().ofType(Integer.class).defaultsTo(300);
        OptionSpec<Long> timeOpt = parser.accepts("time").withRequiredArg().ofType(Long.class).defaultsTo(5000L);


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

        Double bigMass = 100.0;
        Double smallMass = 0.1;
        Double bigR = 50.0;
        Double smallR = 5.0;
        Double brwVelocity = 0.1;
        double square = 500;
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
            if(brownianoImpl.particleImpact2!=null){
	            System.out.println("t: " + (brownTime - t)+"\tTiempo impacto:"+brownianoImpl.getTiempoImpacto()+ "\tp1:"+ brownianoImpl.particleImpact1+"\tp2:"+ brownianoImpl.particleImpact2);
	            System.out.println("\tp1 x:"+ brownianoImpl.particleImpact1.x+"\tp1 y:"+ brownianoImpl.particleImpact1.y+ " mass: "+brownianoImpl.particleImpact1.mass );
	            System.out.println("\tp1 vx:"+ brownianoImpl.particleImpact1.vx+"\tp1 vy:"+ brownianoImpl.particleImpact1.vy);
	            
	            System.out.println("\tp2 x:"+ brownianoImpl.particleImpact2.x+"\tp2 y:"+ brownianoImpl.particleImpact2.y+ " mass: "+brownianoImpl.particleImpact1.mass );
	            System.out.println("\tp2 vx:"+ brownianoImpl.particleImpact2.vx+"\tp2 vy:"+ brownianoImpl.particleImpact2.vy);
	            System.out.println();
            
            }
	        brownianoImpl.evolucionarSistema(particles, brownianoImpl.getTiempoImpacto());
            brownianoImpl.calcularVelocidades(particles);
			exporter.exportBrowniano(writer, particles, t, square, smallR);

        }
        writer.close();
    }

    private static List<Particle> generateParticlesBrown(int numParticles,
                                                         Double square, Double bigMass, Double smallMass, Double bigR, Double smallR, Random rand) {

        List<Particle> particles = new ArrayList<Particle>(numParticles);

        double x = rand.nextDouble() * (square-2*bigR-0.001)+bigR;
        double y = rand.nextDouble() * (square-2*bigR-0.001)+bigR;
        particles.add(new Particle(0, x, y, bigR, bigMass));


        for (int i = 0; i < numParticles-1; i++) {
            int j=i;
            while(j==i){
                double x2 = rand.nextDouble() * (square-2*smallR-0.001)+smallR;
                double y2 = rand.nextDouble() * (square-2*smallR-0.001)+smallR;

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
