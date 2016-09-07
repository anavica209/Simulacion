package ar.edu.itba.simul.group8;

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
import java.util.*;

public class App {

	private static final double MAX_RADIUS = 2.0;

	private static final int BRUTE_FORCE = 0;
	private static final int CIM = 1;
	private static final int CIMCPC = 2;
	
	private static final int OLNOISE = 1;
	private static final int OLDENSITY = 2;
	private static final int OLNONE = 0;

	public static void die(String message) {
		System.err.println("error: " + message);
		System.exit(1);
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
		    die("missing assignment number");
		}

		String assignment = args[0];
		String[] newArgs = Arrays.copyOfRange(args, 1, args.length, String[].class);

		switch (assignment) {
			case "1":
				ar.edu.itba.simul.group8.tp1.Main.main(newArgs);
                return;
			case "2":
				ar.edu.itba.simul.group8.tp2.Main.main(newArgs);
				return;
			case "3":
				ar.edu.itba.simul.group8.tp3.Main.main(newArgs);
				return;
			default:
				die("invalid assignment number");
		}
	}
}
