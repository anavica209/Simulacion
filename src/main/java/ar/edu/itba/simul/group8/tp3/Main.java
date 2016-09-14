package ar.edu.itba.simul.group8.tp3;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ar.edu.itba.simul.group8.common.Particle;
import ar.edu.itba.simul.group8.tp2.XYZExporter;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class Main {

    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();

        OptionSpec<Integer> nOpt = parser.accepts("n").withRequiredArg().ofType(Integer.class).defaultsTo(300);
        OptionSpec<Long> timeOpt = parser.accepts("time").withRequiredArg().ofType(Long.class).defaultsTo(10000L);

        
        OptionSpec<Double> bigMassOpt = parser.accepts("bigMass").withRequiredArg().ofType(Double.class).defaultsTo(100.0);
        OptionSpec<Double> smallMassOpt = parser.accepts("smallMass").withRequiredArg().ofType(Double.class).defaultsTo(0.1);
        OptionSpec<Double> bigROpt = parser.accepts("bigR").withRequiredArg().ofType(Double.class).defaultsTo(50.0);
        OptionSpec<Double> smallROpt = parser.accepts("smallR").withRequiredArg().ofType(Double.class).defaultsTo(5.0);
        OptionSpec<Double> brwVelocityOpt = parser.accepts("brwVelocity").withRequiredArg().ofType(Double.class).defaultsTo(0.1);
        OptionSpec<Double> squareOpt = parser.accepts("square").withRequiredArg().ofType(Double.class).defaultsTo(500.0);
        OptionSpec<Double> dtOpt = parser.accepts("dt").withRequiredArg().ofType(Double.class).defaultsTo(5.0);


        OptionSpec<Integer> typeOpt = parser.accepts("type").withRequiredArg().ofType(Integer.class).defaultsTo(1);
        OptionSpec<Integer> repeticionesOpt = parser.accepts("repeticiones").withRequiredArg().ofType(Integer.class).defaultsTo(10);
        OptionSet options = null;
        try {
            options = parser.parse(args);
        } catch (OptionException e) {
            System.err.println("error: " + e.getMessage());
            System.exit(1);
        }

        Random rand = new Random();
        rand.setSeed(1234);

        int numParticles = options.valueOf(nOpt);
        long time = options.valueOf(timeOpt);

        Double bigMass = options.valueOf(bigMassOpt);
        Double smallMass = options.valueOf(smallMassOpt);
        Double bigR = options.valueOf(bigROpt);
        Double smallR = options.valueOf(smallROpt);
        Double brwVelocity = options.valueOf(brwVelocityOpt);
        double square = options.valueOf(squareOpt);
        double dt=options.valueOf(dtOpt);
        int type=options.valueOf(typeOpt);
        int repeticiones=options.valueOf(repeticionesOpt);
        
		switch(type){
			case 1://grafica por tiempo real
				brownianoReal(numParticles, square, time, bigMass, smallMass, bigR, smallR, brwVelocity, rand, dt);
				break;
			case 2: 
				brownianoFrecuencias(numParticles, square, time, bigMass, smallMass, bigR, smallR, brwVelocity, rand, repeticiones, time);
		        break;	
			case 3: 
				brownianoVelocidades(numParticles, square, time, bigMass, smallMass, bigR, smallR, brwVelocity, rand, repeticiones, "velocidades", dt);
		        break;	
			case 4: 
				brownianoTiemposColisiones( numParticles,  square,  time,  bigMass,
			    		 smallMass,  bigR,  smallR,  brwVelocity,  rand,  repeticiones);
		        break;	
			
			default:
				browniano(numParticles, square, time, bigMass, smallMass, bigR, smallR, brwVelocity, rand);
        }
    }

    
    private static void brownianoVelocidades(int numParticles, double square, long brownTime, Double bigMass,
    		Double smallMass, Double bigR, Double smallR, Double brwVelocity, Random rand, int repeticiones, String filename, Double dt) throws IOException {
    	
    	List<Double> list=new ArrayList<Double>(5000 * repeticiones);
    	XYZExporter exporter = new XYZExporter(Paths.get("./data/"+filename+".csv").toString());

    	for(int repeat=0; repeat<repeticiones; repeat++){
	    	List<Particle> particles = generateParticlesBrown(numParticles, square, bigMass, smallMass, bigR, smallR, rand);
	    	Browniano brownianoImpl= new Browniano(particles, brwVelocity, rand, square);
	    	Double remaining=dt;
	    	double tacum=0;
	    	System.out.println(repeat+"  "+tacum+" "+brownTime);
	    	while(tacum<brownTime){
	    		brownianoImpl.calcularTiempoImpacto(particles);
	    		if(brownianoImpl.getTiempoImpacto()==null){
	//finalizar
	    			break;
	    		}
	    		if(tacum>(brownTime)*(2.5/3.0)){
//	    		if(true){
	    			System.out.println("tacum");
		    		for(Particle p: particles){
		    				double v=Math.sqrt(p.vx*p.vx+p.vy*p.vy);
		    				list.add(v);
		    		}
		    		
		    		break;
	    		}
    			if(remaining > brownianoImpl.getTiempoImpacto()){
    				brownianoImpl.evolucionarSistema(particles, brownianoImpl.getTiempoImpacto());
    				tacum+=brownianoImpl.getTiempoImpacto();
    				remaining=remaining-brownianoImpl.getTiempoImpacto();
    				brownianoImpl.calcularVelocidades(particles);
    			}else{
    				brownianoImpl.evolucionarSistema(particles, remaining);
    				tacum+=remaining;
    				remaining=dt;
    			}
    			
//    			System.out.println(tacum);
    			
    		
	    	}
    	
    	}
    			
    	Writer writer=exporter.startWriter();
    	Double min = Collections.min(list);
    	Double max = Collections.max(list);
    	double inc= (max-min)/20.0;
    	int[] array=new int[21];
    	for(Double v: list){
    		
    		array[(int) ((v-min)/inc)]++;
    	}
    	for(int i=0;i<21;i++){
    		exporter.addCVSLine(writer, (i*inc+min)+"," + (array[i]/repeticiones));
    	}
    	writer.close();
    }
    
    
//    private static void browniano(int numParticles, double square, long brownTime, Double bigMass,
//    		Double smallMass, Double bigR, Double smallR, Double brwVelocity, Random rand, int repeticiones) throws IOException {
//    	
//    	Map<Integer, Integer> map=new HashMap<Integer, Integer>();
//    	XYZExporter exporter = new XYZExporter(Paths.get("./data/particles-colisiones.csv").toString());
//    	int grupo=500;
//    	for(int repeat=0; repeat<repeticiones; repeat++){
//    		System.out.println(repeat);
//	    	List<Particle> particles = generateParticlesBrown(numParticles, square, bigMass, smallMass, bigR, smallR, rand);
//	    	Browniano brownianoImpl= new Browniano(particles, brwVelocity, rand, square);
//	    	int tiAcumulado=0;
//	    	for(long t=0; t<brownTime; t++){
//	    		brownianoImpl.calcularTiempoImpacto(particles);
//	    		if(brownianoImpl.getTiempoImpacto()==null){
//	//finalizar
//	    			break;
//	    		}
//	    		
//	    		
//	    		
//	    		tiAcumulado+=brownianoImpl.getTiempoImpacto();
//	    		map.put(tiAcumulado/grupo, map.get(tiAcumulado/grupo)!=null? map.get(tiAcumulado/grupo)+1:1);
//	    		brownianoImpl.evolucionarSistema(particles, brownianoImpl.getTiempoImpacto());
//	    		brownianoImpl.calcularVelocidades(particles);
//	    	}
//    	
//    	}
//    	Writer writer=exporter.startWriter();
//    	for(Integer elem:map.keySet()){
//    		exporter.addCVSLineInt(writer, elem, map.get(elem)/((double)repeticiones*grupo));
//    	}
//    	writer.close();
//    }

    
    
    private static void brownianoFrecuencias(int numParticles, double square, long brownTime, Double bigMass,
    		Double smallMass, Double bigR, Double smallR, Double brwVelocity, Random rand, int repeticiones, double totalTime) throws IOException {
    	
    	Map<Integer, Integer> map=new HashMap<Integer, Integer>();
    	
    	int bucket = (int) (totalTime/20);
    	for(int i = 1; i <=20; 	i++ )
    		map.put(i*bucket, 0);
    	
    	XYZExporter exporter = new XYZExporter(Paths.get("./data/particles-colisiones.csv").toString());

    	for(int repeat=0; repeat<repeticiones; repeat++){
	    	List<Particle> particles = generateParticlesBrown(numParticles, square, bigMass, smallMass, bigR, smallR, rand);
	    	Browniano brownianoImpl= new Browniano(particles, brwVelocity, rand, square);

	    	double tacum=0;
	    	System.out.println(repeat);
	    	while(tacum<totalTime){
	    		brownianoImpl.calcularTiempoImpacto(particles);
	    		if(brownianoImpl.getTiempoImpacto()==null){
	//finalizar
	    			break;
	    		}
	    		tacum+=  brownianoImpl.getTiempoImpacto();
	    		for(int i = 1; i <=20; 	i++ ){
	    			if(tacum<i*bucket){
	    				map.put(i*bucket, map.get(i*bucket)+1);
	    				break;
	    			}
	    		}
	    		
   				brownianoImpl.evolucionarSistema(particles, brownianoImpl.getTiempoImpacto());
    			
    			brownianoImpl.calcularVelocidades(particles);
    		
	    	}
    	
    	}
    			
    	Writer writer=exporter.startWriter();
    	for(int i=1;i<=20;i++){
    		exporter.addCVSLineInt(writer, i*bucket , map.get(i*bucket)/repeticiones);
    	}
    	writer.close();
    	
    }

    
    private static void brownianoTiemposColisiones(int numParticles, double square, long brownTime, Double bigMass,
    		Double smallMass, Double bigR, Double smallR, Double brwVelocity, Random rand, int repeticiones) throws IOException {
    	
    	double [] array=new double[(int) brownTime];
    	XYZExporter exporter = new XYZExporter(Paths.get("./data/tiempos-colisiones-promedio.csv").toString());
    	
    	for(int repeat=0; repeat<repeticiones; repeat++){
    		List<Particle> particles = generateParticlesBrown(numParticles, square, bigMass, smallMass, bigR, smallR, rand);
    		Browniano brownianoImpl= new Browniano(particles, brwVelocity, rand, square);
    		System.out.println(repeat);
    		for(long t=0; t<brownTime; t++){
    			brownianoImpl.calcularTiempoImpacto(particles);
    			if(brownianoImpl.getTiempoImpacto()==null){
//finalizar
    				break;
    			}
    			array[(int) t]+=brownianoImpl.getTiempoImpacto();
    			brownianoImpl.evolucionarSistema(particles, brownianoImpl.getTiempoImpacto());
    			brownianoImpl.calcularVelocidades(particles);
//exporter.exportBrowniano(writer, particles, t, square, smallR);
//    		exporter.addCVSLine(writer, "" + brownianoImpl.getTiempoImpacto());
    		}
    	}
    	Writer writer=exporter.startWriter();
    	for(int t=0; t<brownTime; t++){
    		array[t]=array[t]/repeticiones;
    		exporter.addCVSLine(writer, "" + array[t]);
    	}
    	writer.close();
    }
    
    
    private static void browniano(int numParticles, double square, long brownTime, Double bigMass,
                                  Double smallMass, Double bigR, Double smallR, Double brwVelocity, Random rand) throws IOException {
        List<Particle> particles = generateParticlesBrown(numParticles, square, bigMass, smallMass, bigR, smallR, rand);

        XYZExporter exporter = new XYZExporter(Paths.get("./data/tiempos-impacto2.csv").toString());
        Browniano brownianoImpl= new Browniano(particles, brwVelocity, rand, square);

        Writer writer=exporter.startWriter();
        for(long t=0; t<brownTime; t++){
	        	brownianoImpl.calcularTiempoImpacto(particles);
	            if(brownianoImpl.getTiempoImpacto()==null){
//					finalizar
	                break;
	            }
	            System.out.println(brownianoImpl.getTiempoImpacto());
	          //if(brownianoImpl.particleImpact2!=null){
//    			System.out.println("t: " + (brownTime - t)+"\tTiempo impacto:"+brownianoImpl.getTiempoImpacto()+ "\tp1:"+ brownianoImpl.particleImpact1+"\tp2:"+ brownianoImpl.particleImpact2);
//				System.out.println("\tp1 x:"+ brownianoImpl.particleImpact1.x+"\tp1 y:"+ brownianoImpl.particleImpact1.y+ " mass: "+brownianoImpl.particleImpact1.mass );
//				System.out.println("\tp1 vx:"+ brownianoImpl.particleImpact1.vx+"\tp1 vy:"+ brownianoImpl.particleImpact1.vy);
//
//				System.out.println("\tp2 x:"+ brownianoImpl.particleImpact2.x+"\tp2 y:"+ brownianoImpl.particleImpact2.y+ " mass: "+brownianoImpl.particleImpact1.mass );
//				System.out.println("\tp2 vx:"+ brownianoImpl.particleImpact2.vx+"\tp2 vy:"+ brownianoImpl.particleImpact2.vy);
//				System.out.println();
//				}
    			brownianoImpl.evolucionarSistema(particles, brownianoImpl.getTiempoImpacto());
		        brownianoImpl.calcularVelocidades(particles);
//				exporter.exportBrowniano(writer, particles, t, square, smallR);
		        exporter.addCVSLine(writer, "" + brownianoImpl.getTiempoImpacto());
        }
        writer.close();
    }
    
    private static void brownianoReal(int numParticles, double square, long brownTime, Double bigMass,
    		Double smallMass, Double bigR, Double smallR, Double brwVelocity, Random rand, double dt) throws IOException {
    	List<Particle> particles = generateParticlesBrown(numParticles, square, bigMass, smallMass, bigR, smallR, rand);
    	
    	XYZExporter exporter = new XYZExporter(Paths.get("./data/particles.xyz").toString());
    	Browniano brownianoImpl= new Browniano(particles, brwVelocity, rand, square);
    	
    	Writer writer=exporter.startWriter();
    	Double remaining=dt;
    	for(long t=0; t<brownTime; t++){
    			brownianoImpl.calcularTiempoImpacto(particles);
    			if(brownianoImpl.getTiempoImpacto()==null){
    				//finalizar
    				break;
    			}
    			System.out.println("t: " + (brownTime - t)+"\tTiempo impacto:"+brownianoImpl.getTiempoImpacto()+ "\tp1:"+ brownianoImpl.particleImpact1+"\tp2:"+ brownianoImpl.particleImpact2);
    			
    			if(remaining > brownianoImpl.getTiempoImpacto()){
    				brownianoImpl.evolucionarSistema(particles, brownianoImpl.getTiempoImpacto());
    				brownianoImpl.calcularVelocidades(particles);
    				remaining=remaining-brownianoImpl.getTiempoImpacto();
    			}else{
    				brownianoImpl.evolucionarSistema(particles, remaining);
    				exporter.exportBrowniano(writer, particles, t, square, smallR);
    				remaining=dt;
    			}
    			

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
