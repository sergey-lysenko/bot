package works.lysenko.scenarios;

import static works.lysenko.Constants.DEFAULT_WEIGHT;
import static works.lysenko.Constants.DEFAULT_SUFFICIENCY_RETRIES;
import static works.lysenko.enums.Severity.S2;
import static works.lysenko.enums.Severity.S3;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.util.Pair;

import works.lysenko.Common;
import works.lysenko.Execution;
import works.lysenko.utils.SortedScenarioSet;

/**
 * This class used for managing of nested scenarios of Abstract Node Scenario
 * 
 * @author Sergii Lysenko
 *
 */
public class Scenarios extends Common {

	private static Pair<Scenario, Double> pair(Scenario c, double d) {
		return new Pair<Scenario, Double>(c, d);
	}

	/**
	 * @param s Scenario
	 * @param r
	 * @return weight coefficient for defined scenario
	 */
	private static double weight(Scenario s, Execution x) {
		String propName = StringUtils.removeStart(s.getClass().getName(), x._root().concat("."));
		String propValue = x.prop(propName, DEFAULT_WEIGHT);
		if (propValue.equals("-"))
			propValue = "NaN";
		Double d = Double.valueOf(propValue);
		return d.equals(Double.POSITIVE_INFINITY) ? Double.MAX_VALUE : d;
	}

	private List<Pair<Scenario, Double>> scenarios;

	private int retries;

	/**
	 * @param x
	 */
	public Scenarios(Execution x) {
		this(DEFAULT_SUFFICIENCY_RETRIES, x);
	}

	/**
	 * Construct Scenarios instance
	 * 
	 * @param sufficiencyRetries number of retries while trying to select a scenario
	 *                           to be executed
	 * @param x
	 */
	public Scenarios(int sufficiencyRetries, Execution x) {
		super(x);
		scenarios = new LinkedList<Pair<Scenario, Double>>();
		retries = sufficiencyRetries;
	}

	/**
	 * Add map of scenarios with weights defined per scenario
	 * 
	 * @param m map of Scenarios and their weights
	 */
	public void add(Map<Scenario, Double> m) {
		m.forEach((k, v) -> {
			scenarios.add(Scenarios.pair(k, v));
		});
	}

	/**
	 * Add single scenario with defined weight
	 * 
	 * @param s scenario to be added
	 * @param d weight coefficient for this scenario
	 */
	public void add(Scenario s, double d) {
		scenarios.add(Scenarios.pair(s, d));
	}

	/**
	 * Add single scenario and read it's weight from properties file
	 * 
	 * @param s Scenario to be adder
	 * @param x
	 * @param r
	 */
	public void add(Scenario s, Execution x) {
		add(s, weight(s, x));
	}

	/**
	 * Add set of scenarios with same defined weight
	 * 
	 * @param ss set of Scenarios to be added
	 * @param d  [same] weight of all scenarios of this set
	 */
	public void add(Set<Scenario> ss, double d) {
		ss.forEach((s) -> {
			scenarios.add(Scenarios.pair(s, d));
		});
	}

	/**
	 * Add set of scenarios and read their weights from properties
	 * 
	 * @param ss Set of Scenarios to be added
	 * @param x
	 * @param r
	 */
	public void add(Set<Scenario> ss, Execution x) {
		if (null != ss)
			ss.forEach((s) -> {
				scenarios.add(Scenarios.pair(s, weight(s, x)));
			});
	}

	/**
	 * @param onlyConfigured defines whether to include only scenarios configured to
	 *                       start, or all available
	 * @return nupber of possible execution paths
	 */
	public int combinations(boolean onlyConfigured) {
		int c = 0;
		for (Pair<Scenario, Double> s : scenarios) {
			c = c + s.getKey().combinations(onlyConfigured);
		}
		return c;
	}

	/**
	 * Execute one of the defined scenarios.
	 * 
	 * As the first step, a copy of scenarios list is created, which is later
	 * referenced as list of candidates. Then,
	 * org.apache.commons.math3.distribution.EnumeratedDistribution.EnumeratedDistribution<Scenario>()
	 * is used to choose a scenario based on weight coefficients.
	 * 
	 * If total weights of all scenarios is zero, no scenario will be selected and
	 * no action will be performed except posting the severe error message to the
	 * test log.
	 * 
	 * Negative weight of any scenario considered as critical exception and leads to
	 * stop of the test.
	 * 
	 * Afterwards the .sufficed() function of the selected scenario is called to
	 * verify compliance to scenario's prerequisites. In case of false result,
	 * warning message posted to the test log and scenario is removed from the list
	 * of candidates.
	 * 
	 * Selection is stopped in one of three cases:
	 * 
	 * - .sufficed() returns 'true'
	 * 
	 * - selection was done more times then configured retries amount
	 * 
	 * - list of candidates become empty (because of all original candidates being
	 * removed due to .sufficed() returning 'false')
	 * 
	 * Failure to select a scenario due to unsufficed prerequisites is not
	 * considered exceptional. In this case, parent scenario execution counted in
	 * (as long as its .action() had already been executed before attempt of nested
	 * scenario selection)
	 */
	public void execute() {
		int retries = this.retries;
		double downstreamWeight = 0.0;
		if (!scenarios.isEmpty()) {
			section("Selecting scenario to execute among " + list(scenarios, (x._upstream() || x._downstream())));
			List<Pair<Scenario, Double>> candidates = new LinkedList<Pair<Scenario, Double>>();
			{ // Cloning scenarios into candidates and populating upstream weights
				for (Pair<Scenario, Double> pair : scenarios) {
					Scenario k = pair.getKey();
					Double v = pair.getValue();
					if (x._upstream()) {
						v = v + k.upstream();
					}
					if (k.executable()) {
						Pair<Scenario, Double> newPair = new Pair<Scenario, Double>(k,
								(v == Double.POSITIVE_INFINITY) ? Double.MAX_VALUE : v);
						candidates.add(newPair);
					} else
						l.logProblem(S3, k.name() + " is not executable");
				}
			}
			AbstractScenario s = null;
			boolean sufficed = false;
			do { // Main selection cycle
				boolean doubleBreak = false;
				try { // Select a scenario among candidates
					s = (AbstractScenario) new EnumeratedDistribution<Scenario>(candidates).sample();
				} catch (MathArithmeticException e) { // Most probably - all weights are zero
					if (x._downstream()) {
						// Cloning candidates into permeates with same default weight
						List<Pair<Scenario, Double>> permeates = new LinkedList<Pair<Scenario, Double>>();
						for (Pair<Scenario, Double> pair : candidates) {
							Scenario k = pair.getKey();
							downstreamWeight = downstream(k);
							Pair<Scenario, Double> newPair = new Pair<Scenario, Double>(k,
									(downstreamWeight == Double.POSITIVE_INFINITY) ? Double.MAX_VALUE
											: downstreamWeight);
							permeates.add(newPair);
						}
						try { // Select a scenario among permeates
							s = (AbstractScenario) new EnumeratedDistribution<Scenario>(permeates).sample();
						} catch (MathArithmeticException e1) {
							l.logProblem(S3, "Unable to select a scenario among " + list(candidates, true));
							doubleBreak = true;
							break;
						}
					} else {
						l.logProblem(S3,
								"Unable to select a scenario among " + list(candidates, true)
										+ (x._downstream() ? " and all nestested scenarios" : "")
										+ ": cumulative probability is zero");
						break;
					}
				} catch (NotPositiveException e) {
					throw new IllegalStateException("Unable to select a scenario among " + list(candidates, true)
							+ ": negative weights are not allowed");
				}
				if (doubleBreak)
					break;
				if (null != s)
					sufficed = s.sufficed(); // to eliminate triple call
				if (!sufficed && null != s) {
					l.logProblem(S3, "Scenario '" + s.shortName() + "' not sufficed");
					Pair<Scenario, Double> toBeRemoved = null;
					for (Pair<Scenario, Double> pair : candidates) {
						if (pair.getKey() == s)
							toBeRemoved = pair;
					}
					candidates.remove(toBeRemoved);
				}
			} while (!sufficed && --retries >= 0 && !candidates.isEmpty());
			sleep(1, true); // tiny holdup in order to avoid having warnings sorted up above retry message
			if (sufficed) {
				l.log(0, "Scenario '" + s.shortName() + "' sufficed, executing ...");
				s.execute();
			}
			if (candidates.isEmpty())
				l.logProblem(S2, "Unable to suffice a scenario among " + list(scenarios, false)
						+ ": all scenarios have unmet requirements or are not executable");
			if (!(retries >= 0))
				l.logProblem(S2, "Scenario selection exausted after " + this.retries + " retries");
		} else
			l.logProblem(S2, "Current Node scenario have no nested scenarios. Is it just a Leaf scenario?");
	}

	protected List<Pair<Scenario, Double>> get() {
		return scenarios;
	}

	/**
	 * @return list of underlying scenarios
	 */
	public Set<Scenario> list() {
		Set<Scenario> c = new SortedScenarioSet();
		for (Pair<Scenario, Double> s : scenarios)
			c.addAll(s.getKey().list());
		return c;
	}

	private String list(Iterable<Pair<Scenario, Double>> list, boolean includeZeroWeight) {
		try {
			String qualifiedName = list.iterator().next().getKey().getClass().getName();
			String[] nameParts = qualifiedName.split("\\."); // this is just twice escaped dot symbol
			String scenarioClassName = nameParts[nameParts.length - 1]; // last one
			String packagePart = qualifiedName.replace(scenarioClassName, "");
			// Creating the set of Strings representing nested scenarios
			Set<String> c = new TreeSet<String>();
			list.forEach((s) -> {
				if (s.getValue() > 0 || includeZeroWeight)
					c.add(s.getKey().getClass().getName().toString().replace(packagePart, ""));
			});
			return packagePart + c.toString();
		} catch (NoSuchElementException e) {
			return "??";
		}
	}

	private Double downstream(Scenario k) {
		return x.downstream(k);
	}

	/**
	 * @return calculated upstream weight
	 */
	public Double upstream() {
		Double p = 0.0;
		for (Pair<Scenario, Double> s : scenarios) {
			// if (!(s.getValue()).isNaN()) {
			if (s.getKey().executable()) {
				p = p + s.getValue(); // weight of a scenario
				p = p + s.getKey().upstream(); // weight of underlying
			}
		}
		return p;
	}
}
