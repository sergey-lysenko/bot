package works.lysenko;

import static works.lysenko.Constants.CONFIGURATION_CONJOINT;
import static works.lysenko.Constants.CONFIGURATION_CYCLES;
import static works.lysenko.Constants.CONFIGURATION_DEBUG;
import static works.lysenko.Constants.CONFIGURATION_DOWNSTREAM;
import static works.lysenko.Constants.CONFIGURATION_ROOT;
import static works.lysenko.Constants.CONFIGURATION_UPSTREAM;
import static works.lysenko.Constants.DEFAULT_CONJOINT;
import static works.lysenko.Constants.DEFAULT_CYCLES;
import static works.lysenko.Constants.DEFAULT_DEBUG;
import static works.lysenko.Constants.DEFAULT_DOWNSTREAM;
import static works.lysenko.Constants.DEFAULT_ROOT;
import static works.lysenko.Constants.DEFAULT_UPSTREAM;
import static works.lysenko.Constants.DEFAULT_WEIGHT;
import static works.lysenko.Constants.GENERATED_CONFIG_FILE;
import static works.lysenko.enums.Ansi.y;
import static works.lysenko.enums.Severity.S1;
import static works.lysenko.enums.Severity.S2;
import static works.lysenko.enums.Severity.S3;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import works.lysenko.scenarios.Scenario;
import works.lysenko.scenarios.ScenarioLoader;
import works.lysenko.scenarios.Scenarios;
import works.lysenko.utils.SortedStringSet;

/**
 * @author Sergii Lysenko
 */
public class Cycles {

	/**
	 * Associated Execution object
	 */
	public Execution x;
	/**
	 * Amount of test cycles left to be executed in current run
	 */
	public int cyclesToDo;
	protected Scenarios scenarios;
	private String root = DEFAULT_ROOT;

	/**
	 * @param s
	 * @param x
	 */
	public Cycles(Execution x) {
		super();
		this.x = x;
		root = x._root();
		scenarios = new Scenarios(x);
		scenarios.add(ScenarioLoader.read(root, x), x);
		cyclesToDo = x._cycles();
		x.cycles = this;
	}

	/**
	 * @param ss
	 * @param x
	 */
	public Cycles(Set<Scenario> ss, Execution x) {
		super();
		this.x = x;
		scenarios = new Scenarios(x);
		scenarios.add(ss, x);
		cyclesToDo = x._cycles();
		x.cycles = this;
	}

	/**
	 * @param s
	 * @param x
	 */
	public Cycles(String s, Execution x) {
		super();
		this.x = x;
		scenarios = new Scenarios(x);
		scenarios.add(ScenarioLoader.read(s, x), x);
		cyclesToDo = x._cycles();
		x.cycles = this;
	}

	/**
	 * @return default configuration properties
	 */
	public Set<String> defConf() {
		Set<String> c = new SortedStringSet();
		c.add("_" + CONFIGURATION_ROOT + " = " + root);
		c.add("_" + CONFIGURATION_DEBUG + " = " + DEFAULT_DEBUG);
		c.add("_" + CONFIGURATION_CYCLES + " = " + DEFAULT_CYCLES);
		c.add("_" + CONFIGURATION_CONJOINT + " = " + DEFAULT_CONJOINT);
		c.add("_" + CONFIGURATION_UPSTREAM + " = " + DEFAULT_UPSTREAM);
		c.add("_" + CONFIGURATION_DOWNSTREAM + " = " + DEFAULT_DOWNSTREAM);
		scenarios.list().forEach(s -> {
			c.add(StringUtils.removeStart(s.name(), x._root().concat(".")) + " = " + DEFAULT_WEIGHT);
		});
		return c;
	}

	/**
	 * Execute configured cycles
	 */
	public void execute() {
		try {
			if (cyclesToDo == 0)
				x.l.logProblem(S2, "No test cycles were perfomed");
			x.l.log(0, "Executing " + y(x._cycles()) + " cycle" + (x._cycles() > 1 ? "s" : "") + " of "
					+ x.testDescription());
			while (cyclesToDo > 0) {
				scenarios.execute();
				x.l.logln();
				if (--cyclesToDo > 0)
					x.l.log(0, "Cycles to do: " + y(cyclesToDo));
			}
			x.l.log(0, y(x.currentCycle()) + " cycle" + (x.currentCycle() > 1 ? "s" : "") + " of " + x.testDescription() + " done");
			x.o.writeDefConf(defConf());
			if (x.propEmpty())
				x.l.logProblem(S3,
						"Test properties are absent. Wrong configuration? Template of Test Run Properties file '"
								+ GENERATED_CONFIG_FILE + "' was updated");
			if (x.service != null) {
				x.l.log("Closing test service ...");
				x.service.close();
				x.l.log(" ... done.");
			}
		} catch (Exception e) {
			x.exception = e;
			x.l.logProblem(S1, "Uncaught exception during cycles execution: " + e.getMessage());
			throw e;
		}
	}

	protected Set<Scenario> scenariosList() {
		return scenarios.list();
	}
}
