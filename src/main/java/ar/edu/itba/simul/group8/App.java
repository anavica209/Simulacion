package ar.edu.itba.simul.group8;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.IOException;
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

		OptionSpec<Integer> generateOvitoOpt = parser.accepts("ovito").withRequiredArg().ofType(Integer.class)
				.defaultsTo(1);

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
		boolean generateOvito = options.valueOf(generateOvitoOpt) == 1;

		for (double lindex = lstart; lindex <= l;) {
			System.out.println("-----------Valor de L: " + lindex + "  -----------");

			for (int i = 0; i < ntimes; i++) {
				NeighborSearch search;
				List<Particle> particles = generateParticles(numParticles, lindex);
				switch (searchType) {
				case BRUTE_FORCE:
					search = new BruteForceSearch(particles, lindex, m);
					break;
				case CIM:
					search = new CellIndexSearch(particles, lindex, m);
				default:
					search = new CellIndexSearchCPC(particles, lindex, m);
				}

				Neighbors neighbors = search.timedSearch(20.0);

				// System.out.println("Neighbors: " +
				// neighbors.getAllNeighbors().toString());
//				System.out.println("Execution time: " + neighbors.getExecutionTime());
//para copiar datos para graficar
				System.out.println(neighbors.getExecutionTime());

				if (generateOvito) {
					XYZExporter exporter = new XYZExporter(Paths.get("./data/particles.xyz").toString());

					int random = rand.nextInt(particles.size());
					Particle selected = particles.get(random);
					exporter.exportWithSelection(particles, selected, neighbors.getNeighbors(selected));
				}
			}
			lindex += linc;
		}

	}

	public static List<Particle> generateParticles(int numParticles, double l) {
		Random rand = new Random();

		List<Particle> particles = new ArrayList<>(numParticles);

		for (int i = 0; i < numParticles; i++) {
			// TODO: make sure we never get the upper boundary
			double x = rand.nextDouble() * l;
			double y = rand.nextDouble() * l;
			double radius = rand.nextDouble() * MAX_RADIUS;
			particles.add(new Particle(i + 1, x, y, radius));
		}

		return particles;
	}
}
