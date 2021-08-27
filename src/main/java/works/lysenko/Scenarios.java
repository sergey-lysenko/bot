package works.lysenko;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.util.Pair;

/**
 * This class used for managing of nested scenarios of Abstract Node Scenario
 * 
 * @author Sergii Lysenko
 *
 */
public class Scenarios extends Common {

	private List<Pair<Scenario, Double>> scenarios;
	private int retries;

	public Scenarios(Run r) {
		this(Constants.DEFAULT_SUFFICIENCY_RETRIES, r);
	}

	/**
	 * Construct Scenarios instance
	 * 
	 * @param sufficiencyRetries number of retries while trying to select a scenario
	 *                           to be executed
	 */
	public Scenarios(int sufficiencyRetries, Run r) {
		super(r);
		scenarios = new LinkedList<Pair<Scenario, Double>>();
		retries = sufficiencyRetries;
	}

	private static Pair<Scenario, Double> pair(Scenario c, double d) {
		return new Pair<Scenario, Double>(c, d);
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
		if (!scenarios.isEmpty()) {
			section("Selecting scenario to execute among " + list(scenarios, false));
			// Cloning scenarios into candidates
			List<Pair<Scenario, Double>> candidates = new LinkedList<Pair<Scenario, Double>>();
			for (Pair<Scenario, Double> pair : scenarios) {
				Pair<Scenario, Double> newPair = new Pair<Scenario, Double>(pair.getKey(), pair.getValue());
				candidates.add(newPair);
			}
			AbstractScenario s = null;
			boolean sufficed = false;
			do {
				try {
					s = (AbstractScenario) new EnumeratedDistribution<Scenario>(candidates).sample();
				} catch (MathArithmeticException e) {
					r.problem("[WARNING] " + "Unable to select a scenario among " + list(candidates, true)
							+ ": union weight is zero");
					break;
				} catch (NotPositiveException e) {
					throw new IllegalStateException("Unable to select a scenario among " + list(candidates, true)
							+ ": negative weights are not allowed");
				}
				sufficed = s.sufficed(); // to eliminate triple call
				if (!sufficed) {
					r.problem("[NOTICE] " + s.name() + " is not sufficed");
					Pair<Scenario, Double> toBeRemoved = null;
					for (Pair<Scenario, Double> pair : candidates) {
						if (pair.getKey() == s)
							toBeRemoved = pair;
					}
					candidates.remove(toBeRemoved);
				}
			} while (!sufficed && retries-- > 0 && !candidates.isEmpty());
			AbstractScenario parent = r.current;
			if (sufficed) {
				log(0, s.name() + " sufficed");
				s.execute();
				log(0, s.name() + " done");
			}
			r.current = parent;
			if (candidates.isEmpty())
				r.problem("[WARNING] " + "Unable to suffice a scenario among " + list(scenarios, false)
						+ ": all scenarios have unmet requirements");
			if (!(retries > 0))
				r.problem("[WARNING] " + "Scenario selection took more than " + this.retries + " retries");
		}
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
	 * Add single scenario and read it's weight from properties file
	 * 
	 * @param s Scenario to be adder
	 * @param r
	 */
	public void add(Scenario s, Run r) {
		add(s, weight(s, r));
	}

	/**
	 * Add set of scenarios and read their weights from properties
	 * 
	 * @param ss Set of Scenarios to be added
	 * @param r
	 */
	public void add(Set<Scenario> ss, Run r) {
		if (null != ss)
			ss.forEach((s) -> {
				scenarios.add(Scenarios.pair(s, weight(s, r)));
			});
	}

	private String list(Iterable<Pair<Scenario, Double>> list, boolean includeZeroWeight) {
		// There is an algorithmic assumption that all scenarios in a list are from same
		// package
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
	}

	/**
	 * @return default configuration for all nested scenarios
	 */
	public Set<String> defConf() {
		Set<String> c = new TreeSet<String>((new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		}));
		scenarios.forEach((s) -> {
			c.addAll(s.getKey().defConf());
		});
		return c;
	}

	/**
	 * @param s Scenario
	 * @param r
	 * @return weight coefficient for defined scenario
	 */
	private double weight(Scenario s, Run r) {
		Double d = Double.valueOf(r.prop(s.getClass().getName(), Constants.DEFAULT_SCENARIO_WEIGHT));
		return d.equals(Double.POSITIVE_INFINITY) ? Double.MAX_VALUE : d;
	}
}
