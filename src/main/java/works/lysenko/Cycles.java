package works.lysenko;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Cycles extends Common {

	public int cycles;
	private static Scenarios scenarios;

	public Cycles(Set<Scenario> ss, Run r) {
		super(r);
		scenarios = new Scenarios(r);
		scenarios.add(ss, r);
		cycles = r.cycles();
		r.cycles = this;
	}

	public void execute() {
		if (cycles == 0)
			r.problem("[WARNING] No test cycles were perfomed");
		while (cycles > 0) {
			scenarios.execute();
			logln();
			if (--cycles > 0)
				log("Cycles to go: " + colorize(String.valueOf(cycles), Color.YELLOW_BOLD));
		}
		Run.writeDefConf(defConf(), Constants.GENERATED_CONFIG_FILE);
		if (r.propEmpty())
			r.problem("[NOTICE] Template of Test Run Properties file '" + Constants.GENERATED_CONFIG_FILE
					+ "' was updated");
	}

	public Set<String> defConf() {
		Set<String> c = new TreeSet<String>((new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		}));
		c.add("cycles = " + Constants.DEFAULT_CYCLES_COUNT);
		scenarios.defConf().forEach((s) -> {
			c.add(s);
		});
		return c;
	}
}
