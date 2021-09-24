package works.lysenko;

import static works.lysenko.Constants.CONFIGURATION_CONJOINT;
import static works.lysenko.Constants.CONFIGURATION_CYCLES;
import static works.lysenko.Constants.CONFIGURATION_DOWNSTREAM;
import static works.lysenko.Constants.CONFIGURATION_UPSTREAM;
import static works.lysenko.Constants.DEFAULT_CONJOINT;
import static works.lysenko.Constants.DEFAULT_CYCLES;
import static works.lysenko.Constants.DEFAULT_DOWNSTREAM;
import static works.lysenko.Constants.DEFAULT_UPSTREAM;
import static works.lysenko.Constants.DEFAULT_SCENARIO_WEIGHT;
import static works.lysenko.Constants.GENERATED_CONFIG_FILE;
import static works.lysenko.utils.Severity.S2;
import static works.lysenko.utils.Severity.S3;

import java.util.Set;

import works.lysenko.scenarios.Scenario;
import works.lysenko.scenarios.ScenarioLoader;
import works.lysenko.scenarios.Scenarios;
import works.lysenko.utils.Ansi;
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

	private String y(Object s) {
		return Ansi.colorize(String.valueOf(s), Ansi.YELLOW_BOLD);
	}

	/**
	 * Execute configured cycles
	 */
	public void execute() {
		if (cyclesToDo == 0)
			x.l.logProblem(S2, "No test cycles were perfomed");
		x.l.log(0, "Executing " + y(cyclesToDo) + " cycles of " + y(x.name) + " on " + y(x.domain));
		while (cyclesToDo-- > 0) {
			scenarios.execute();
			x.l.logln();
			if (cyclesToDo > 0)
				x.l.log(0, "Cycles to do: " + y(cyclesToDo));
		}
		x.o.writeDefConf(defConf());
		if (x.propEmpty())
			x.l.logProblem(S3, "Test properties are absent. Wrong configuration? Template of Test Run Properties file '"
					+ GENERATED_CONFIG_FILE + "' was updated");
	}

	/**
	 * @return default configuration properties
	 */
	public Set<String> defConf() {
		Set<String> c = new SortedStringSet();
		c.add("_" + CONFIGURATION_CYCLES + " = " + DEFAULT_CYCLES);
		c.add("_" + CONFIGURATION_CONJOINT + " = " + DEFAULT_CONJOINT);
		c.add("_" + CONFIGURATION_UPSTREAM + " = " + DEFAULT_UPSTREAM);
		c.add("_" + CONFIGURATION_DOWNSTREAM + " = " + DEFAULT_DOWNSTREAM);
		scenarios.list(false, false).forEach((s) -> {
			c.add(s + " = " + DEFAULT_SCENARIO_WEIGHT);
		});
		return c;
	}

	protected Set<String> scenariosList(boolean shortened, boolean decorated) {
		return scenarios.list(shortened, decorated);
	}
}
