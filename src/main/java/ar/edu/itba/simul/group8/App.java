package ar.edu.itba.simul.group8;

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

public class App {

	private static final double MAX_RADIUS = 2.0;

	private static final int BRUTE_FORCE = 0;
	private static final int CIM = 1;
	private static final int CIMCPC = 2;

	public static void main(String[] args) throws IOException {
		OptionParser parser = new OptionParser();

		OptionSpec<Integer> nOpt = parser.accepts("n").withRequiredArg().ofType(Integer.class).defaultsTo(200);
		OptionSpec<Integer> lOpt = parser.accepts("l").withRequiredArg().ofType(Integer.class).defaultsTo(100);
		OptionSpec<Integer> mOpt = parser.accepts("m").withRequiredArg().ofType(Integer.class).defaultsTo(10);
		OptionSpec<Integer> ntimesOpt = parser.accepts("ntimes").withRequiredArg().ofType(Integer.class).defaultsTo(10);
		OptionSpec<Integer> lincOpt = parser.accepts("linc").withRequiredArg().ofType(Integer.class).defaultsTo(10);
		OptionSpec<Integer> lstartOpt = parser.accepts("lstart").withRequiredArg().ofType(Integer.class).defaultsTo(10);
		OptionSpec<Integer> searchOpt = parser.accepts("search").withRequiredArg().ofType(Integer.class)
				.defaultsTo(CIMCPC);

		OptionSpec<Boolean> generateOvitoOpt = parser.accepts("ovito").withRequiredArg().ofType(Boolean.class)
				.defaultsTo(true);
        OptionSpec<Boolean> statsOpt = parser.accepts("stats").withRequiredArg().ofType(Boolean.class).defaultsTo(false);
		OptionSpec<Boolean> optimumCellsOpt = parser.accepts("optimumCells").withRequiredArg().ofType(Boolean.class).defaultsTo(false);
		OptionSpec<Boolean> efficiencyOpt = parser.accepts("efficiency").withRequiredArg().ofType(Boolean.class).defaultsTo(false);
		
		OptionSpec<Boolean> offLatticeOpt = parser.accepts("offLattice").withRequiredArg().ofType(Boolean.class).defaultsTo(true);
		OptionSpec<Double> modVelocityOpt = parser.accepts("modVelocity").withRequiredArg().ofType(Double.class).defaultsTo(0.03);
		OptionSpec<Long> timeOpt = parser.accepts("time").withRequiredArg().ofType(Long.class).defaultsTo(500L);
		OptionSpec<Double> noiseOpt = parser.accepts("noise").withRequiredArg().ofType(Double.class).defaultsTo(0.01);

		
		OptionSet options = null;
		try {
			options = parser.parse(args);
		} catch (OptionException e) {
			System.err.println("error: " + e.getMessage());
			System.exit(1);
		}

		Random rand = new Random();

		int numParticles = options.valueOf(nOpt);
		double l = options.valueOf(lOpt);
		int m = options.valueOf(mOpt);
		int ntimes = options.valueOf(ntimesOpt);
		int linc = options.valueOf(lincOpt);
		int lstart = options.valueOf(lstartOpt);
		int searchType = options.valueOf(searchOpt);
		boolean generateOvito = options.valueOf(generateOvitoOpt);
		boolean stats = options.valueOf(statsOpt);
		boolean optimumCells = options.valueOf(optimumCellsOpt);
		boolean efficiency = options.valueOf(efficiencyOpt);
		boolean offLattice = options.valueOf(offLatticeOpt);

		double modVelocity = options.valueOf(modVelocityOpt);
		long time = options.valueOf(timeOpt);
		double noise = options.valueOf(noiseOpt);

		if (stats) {
			for (double lindex = lstart; lindex <= l;) {

				long sumTime = 0;

				for (int i = 0; i < ntimes; i++) {

					List<Particle> particles = generateParticles(numParticles, lindex, null);
					Neighbors neighbors = runAlgorithm(particles, searchType, numParticles, lindex, m);
					sumTime += neighbors.getExecutionTime();

					if (generateOvito) {
						XYZExporter exporter = new XYZExporter(Paths.get("./data/particles.xyz").toString());

						int random = rand.nextInt(particles.size());
						Particle selected = particles.get(random);
						exporter.exportWithSelection(particles, selected, neighbors.getNeighbors(selected));
					}
				}

				double avgTime = ((double) sumTime) / ntimes;
				System.out.println(lindex + "," + avgTime);

				lindex += linc;
			}
		} else if (optimumCells) {

			runOptimumCells(20, 1, 0.25);

		} else if (efficiency) {

			runEfficiency(20, 1, 0.25);

		} else if(offLattice){
			
			
			List<Particle> particles = generateParticles(numParticles, l, null);
		    
			XYZExporter exporter = new XYZExporter(Paths.get("./data/particles.xyz").toString());
			OffLattice offLatticeImpl= new OffLattice(particles, modVelocity, noise, rand);
			
			Writer writer=exporter.startLattice();
			for(long t=0; t<time; t++){
				Neighbors neighbors = runAlgorithm(particles, searchType, numParticles, l, m);
				offLatticeImpl.calcularVelocidades(neighbors.getAllNeighbors());
				particles=offLatticeImpl.reposicionarParticulas();
				
				exporter.exportOffLattice(writer, particles, t);
			}
			writer.close();

		}else{
			List<Particle> particles = generateParticles(numParticles, l, null);
		    Neighbors neighbors = runAlgorithm(particles, searchType, numParticles, l, m);

			System.out.println("Neighbors: " + neighbors.getAllNeighbors().toString());
			System.out.println("Execution time: " + neighbors.getExecutionTime());
		}
	}

	private static void runEfficiency(int l, double radius, double particleRadius) {

		for (int n = 100; n < 200; n += 10) {
			for (int m = 10; m < 20; m++) {
				List<Particle> particles = generateParticles(100, l, particleRadius);
				Neighbors neighbors = runAlgorithm(particles, CIM, n, l, m);
				long time = neighbors.getExecutionTime();

				neighbors = runAlgorithm(particles, BRUTE_FORCE, n, l, m);
				long bruteForceTime = neighbors.getExecutionTime();

				System.out.println(String.format("%d;%d;%d;%d", n, m * m, time, bruteForceTime));
			}
		}
	}
	private static void runOptimumCells(int l, double radius, double particleRadius) {

		for (int n = 100; n < 200; n += 10) {
			for (int m = 10; m < 20; m++) {
				List<Particle> particles = generateParticles(100, l, particleRadius);
				Neighbors neighbors = runAlgorithm(particles, CIM, n, l, m);
				long time = neighbors.getExecutionTime();
				double density = ((double) n) / (l * l);

				System.out.println(String.format("%d;%d;%f;%d", n, m * m, density, time));
			}
        }
	}

	public static Neighbors runAlgorithm(List<Particle> particles, int searchType, int numParticles, double lindex, int m) {
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

		return search.timedSearch(20.0);
	}

	public static List<Particle> generateParticles(int numParticles, double l, Double fixedRadius) {
		Random rand = new Random();

		List<Particle> particles = new ArrayList<Particle>(numParticles);

		for (int i = 0; i < numParticles; i++) {
			// TODO: make sure we never get the upper boundary
			double x = rand.nextDouble() * l;
			double y = rand.nextDouble() * l;
			double radius = (fixedRadius != null) ? fixedRadius : rand.nextDouble() * MAX_RADIUS;
			particles.add(new Particle(i + 1, x, y, radius));
		}

		return particles;
	}
}
