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
import static works.lysenko.Constants.u0020;
import static works.lysenko.Constants.u002E;
import static works.lysenko.Constants.u003D;
import static works.lysenko.Constants.u005F;
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
		this.root = x._root();
		this.scenarios = new Scenarios(x);
		this.scenarios.add(ScenarioLoader.read(this.root, x), x);
		this.cyclesToDo = x._cycles();
		x.cycles = this;
	}

	/**
	 * @param ss
	 * @param x
	 */
	public Cycles(Set<Scenario> ss, Execution x) {
		super();
		this.x = x;
		this.scenarios = new Scenarios(x);
		this.scenarios.add(ss, x);
		this.cyclesToDo = x._cycles();
		x.cycles = this;
	}

	/**
	 * @param s
	 * @param x
	 */
	public Cycles(String s, Execution x) {
		super();
		this.x = x;
		this.scenarios = new Scenarios(x);
		this.scenarios.add(ScenarioLoader.read(s, x), x);
		this.cyclesToDo = x._cycles();
		x.cycles = this;
	}

	/**
	 * @return default configuration properties
	 */
	public Set<String> defConf() {
		String e = u0020 + u003D + u0020;
		Set<String> c = new SortedStringSet();
		c.add(u005F + CONFIGURATION_ROOT + e + this.root);
		c.add(u005F + CONFIGURATION_DEBUG + e + DEFAULT_DEBUG);
		c.add(u005F + CONFIGURATION_CYCLES + e + DEFAULT_CYCLES);
		c.add(u005F + CONFIGURATION_CONJOINT + e + DEFAULT_CONJOINT);
		c.add(u005F + CONFIGURATION_UPSTREAM + e + DEFAULT_UPSTREAM);
		c.add(u005F + CONFIGURATION_DOWNSTREAM + e + DEFAULT_DOWNSTREAM);
		this.scenarios.list().forEach(s -> {
			c.add(StringUtils.removeStart(s.name(), this.x._root().concat(u002E)) + e + DEFAULT_WEIGHT);
		});
		return c;
	}

	/**
	 * Execute configured cycles
	 */
	@SuppressWarnings({ "boxing", "nls" })
	public void execute() {
		try {
			if (this.cyclesToDo == 0)
				this.x.l.logProblem(S2, "No test cycles were perfomed");
			this.x.l.log(0, "Executing " + y(this.x._cycles()) + " cycle(s) of " + this.x.testDescription());
			while (this.cyclesToDo > 0) {
				this.scenarios.execute();
				this.x.l.logln();
				if (--this.cyclesToDo > 0)
					this.x.l.log(0, "Cycles to do: " + y(this.cyclesToDo));
			}
			Output.writeDefConf(defConf());
			if (this.x.propEmpty())
				this.x.l.logProblem(S3,
						"Test properties are absent. Wrong configuration? Template of Test Run Properties file '"
								+ GENERATED_CONFIG_FILE + "' was updated");
			if (this.x.service != null) {
				this.x.l.log("Closing test service ...");
				this.x.service.close();
				this.x.l.log(" ... done.");
			}
		} catch (Exception e) {
			this.x.exception = e;
			this.x.l.logProblem(S1, "Uncaught exception during cycles execution: " + e.getMessage());
			throw e;
		}
	}

	protected Set<Scenario> scenariosList() {
		return this.scenarios.list();
	}
}
