package works.lysenko.scenarios;

import static works.lysenko.Constants.DEFAULT_SUFFICIENCY_RETRIES;
import static works.lysenko.Constants.DEFAULT_WEIGHT;
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

	@SuppressWarnings("boxing")
	private static Pair<Scenario, Double> pair(Scenario c, double d) {
		return new Pair<>(c, d);
	}

	/**
	 * @param s {@link works.lysenko.scenarios.Scenario} of interest
	 * @param x reference to active {@link works.lysenko.Execution} instance
	 * @return weight coefficient for defined Scenario in scope of the Execution
	 */
	@SuppressWarnings("boxing")
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
	 * Construct new {@link works.lysenko.scenarios.Scenarios} instance
	 * 
	 * @param x reference to active {@link works.lysenko.Execution} instance
	 */
	public Scenarios(Execution x) {
		this(DEFAULT_SUFFICIENCY_RETRIES, x);
	}

	/**
	 * Construct new {@link works.lysenko.scenarios.Scenarios} instance
	 * 
	 * @param sufficiencyRetries number of retries while trying to select a scenario
	 *                           to be executed
	 * @param x                  reference to active {@link works.lysenko.Execution}
	 *                           instance
	 */
	public Scenarios(int sufficiencyRetries, Execution x) {
		super(x);
		this.scenarios = new LinkedList<>();
		this.retries = sufficiencyRetries;
	}

	/**
	 * Add map of {@link works.lysenko.scenarios.Scenario} and weights defined per
	 * Scenario
	 * 
	 * @param m map of Scenarios and their weights
	 */
	@SuppressWarnings("boxing")
	public void add(Map<Scenario, Double> m) {
		m.forEach((k, v) -> {
			this.scenarios.add(Scenarios.pair(k, v));
		});
	}

	/**
	 * Add single {@link works.lysenko.scenarios.Scenario} with defined weight
	 *
	 * @param s Scenario to be added
	 * @param dw weight coefficient for this Scenario
	 */
	public void add(Scenario s, double dw) {
		this.scenarios.add(Scenarios.pair(s, dw));
	}

	/**
	 * Add single {@link works.lysenko.scenarios.Scenario} and read it's weight from
	 * execution configuration file
	 *
	 * @param s Scenario to be added
	 * @param ex reference to active {@link works.lysenko.Execution} instance
	 */
	public void add(Scenario s, Execution ex) {
		add(s, weight(s, ex));
	}

	/**
	 * Add set of {@link works.lysenko.scenarios.Scenario} with same weight
	 *
	 * @param ss set of Scenarios to be added
	 * @param dw  [same] weight of all Scenarios of this set
	 */
	public void add(Set<Scenario> ss, double dw) {
		ss.forEach(s -> {
			this.scenarios.add(Scenarios.pair(s, dw));
		});
	}

	/**
	 * Add set of {@link works.lysenko.scenarios.Scenario} and read their weights
	 * from execution configuration file
	 *
	 * @param ss set of Scenarios to be added
	 * @param ex reference to active {@link works.lysenko.Execution} instance
	 */
	public void add(Set<Scenario> ss, Execution ex) {
		if (null != ss)
			ss.forEach(s -> {
				this.scenarios.add(Scenarios.pair(s, weight(s, ex)));
			});
	}

	/**
	 * Calculate number of possible execution paths
	 * 
	 * @param onlyConfigured defines whether to include only scenarios configured to
	 *                       start, or all available ones
	 * @return number of possible execution paths
	 */
	public int combinations(boolean onlyConfigured) {
		int c = 0;
		for (Pair<Scenario, Double> s : this.scenarios)
			c = c + s.getKey().combinations(onlyConfigured);
		return c;
	}

	@SuppressWarnings("boxing")
	private Double downstream(Scenario k) {
		return this.x.downstream(k);
	}

	/**
	 * Execute one of the defined scenarios.
	 *
	 * As the first step, a copy of scenarios list is created, which is later
	 * referenced as list of candidates. Then,
	 * {@link org.apache.commons.math3.distribution.EnumeratedDistribution} is used
	 * to choose a scenario based on weight coefficients.
	 *
	 * If sum of weights of all scenarios is zero, no scenario will be selected and
	 * no action will be performed except posting the error message to the test log.
	 *
	 * Negative weight of any scenario considered as critical exception and leads to
	 * stop of the test.
	 *
	 * Afterwards the {@link works.lysenko.scenarios.Scenario#sufficed()} method of
	 * the selected scenario is called to verify compliance to scenario's
	 * prerequisites. In case of false result, warning message posted to the test
	 * log and scenario is removed from the list of candidates.
	 *
	 * Selection is stopped in one of three cases:
	 *
	 * - {@link works.lysenko.scenarios.Scenario#sufficed()} returns {@value true}
	 *
	 * - there were more selection attempts then configured retries amount
	 *
	 * - list of candidates become empty (because of all original candidates being
	 * removed due to their {@link works.lysenko.scenarios.Scenario#sufficed()}
	 * returning {@value false})
	 *
	 * Failure to select a scenario due to insufficed prerequisites is not
	 * considered exceptional. In this case, parent scenario execution counted in
	 * (as long as its {@link works.lysenko.scenarios.Scenario#action()} had already
	 * been executed before attempt of nested scenario selection)
	 */
	@SuppressWarnings("boxing")
	public void execute() {
		int ret = this.retries;
		double downstreamWeight = 0.0;
		if (!this.scenarios.isEmpty()) {
			section("Selecting scenario to execute among "
					+ list(this.scenarios, this.x._upstream() || this.x._downstream()));
			List<Pair<Scenario, Double>> candidates = new LinkedList<>();
			{ // Cloning of scenarios into candidates and populating upstream weights
				for (Pair<Scenario, Double> pair : this.scenarios) {
					Scenario k = pair.getKey();
					Double v = pair.getValue();
					if (this.x._upstream())
						v = v + k.upstream();
					if (k.executable()) {
						Pair<Scenario, Double> newPair = new Pair<>(k,
								v == Double.POSITIVE_INFINITY ? Double.MAX_VALUE : v);
						candidates.add(newPair);
					} else
						this.l.logProblem(S3, k.name() + " is not executable");
				}
			}
			AbstractScenario s = null;
			boolean sufficed = false;
			do { // Main selection cycle
				boolean doubleBreak = false;
				try { // Select a scenario among candidates
					s = (AbstractScenario) new EnumeratedDistribution<>(candidates).sample();
				} catch (@SuppressWarnings("unused") MathArithmeticException e) { // Most probably - all weights are
																					// zero
					if (this.x._downstream()) {
						// Cloning candidates into permeates with same default weight
						List<Pair<Scenario, Double>> permeates = new LinkedList<>();
						for (Pair<Scenario, Double> pair : candidates) {
							Scenario k = pair.getKey();
							downstreamWeight = downstream(k);
							Pair<Scenario, Double> newPair = new Pair<>(k,
									downstreamWeight == Double.POSITIVE_INFINITY ? Double.MAX_VALUE : downstreamWeight);
							permeates.add(newPair);
						}
						try { // Select a scenario among permeates
							s = (AbstractScenario) new EnumeratedDistribution<>(permeates).sample();
						} catch (@SuppressWarnings("unused") MathArithmeticException e1) {
							this.l.logProblem(S3, "Unable to select a scenario among " + list(candidates, true));
							doubleBreak = true;
							break;
						}
					} else {
						this.l.logProblem(S3,
								"Unable to select a scenario among " + list(candidates, true)
										+ (this.x._downstream() ? " and all nestested scenarios" : "")
										+ ": cumulative probability is zero");
						break;
					}
				} catch (@SuppressWarnings("unused") NotPositiveException e) {
					throw new IllegalStateException("Unable to select a scenario among " + list(candidates, true)
							+ ": negative weights are not allowed");
				}
				if (doubleBreak)
					break;
				if (null != s)
					sufficed = s.sufficed(); // to eliminate triple call
				if (!sufficed && null != s) {
					this.l.logProblem(S3, "Scenario '" + s.shortName() + "' not sufficed");
					Pair<Scenario, Double> toBeRemoved = null;
					for (Pair<Scenario, Double> pair : candidates)
						if (pair.getKey() == s)
							toBeRemoved = pair;
					candidates.remove(toBeRemoved);
				}
			} while (!sufficed && --ret >= 0 && !candidates.isEmpty());
			sleep(1, true); // tiny holdup in order to avoid having warnings sorted up above retry message
			if (sufficed) {
				if (null != s) {
					this.l.log(0, "Scenario '" + s.shortName() + "' sufficed, executing ...");
					s.execute();
				}
			}
			if (candidates.isEmpty())
				this.l.logProblem(S2, "Unable to suffice a scenario among " + list(this.scenarios, false)
						+ ": all scenarios have unmet requirements or are not executable");
			if (!(ret >= 0))
				this.l.logProblem(S2, "Scenario selection exausted after " + this.retries + " retries");
		} else
			this.l.logProblem(S2, "Current Node scenario have no nested scenarios. Is it just a Leaf scenario?");
	}

	protected List<Pair<Scenario, Double>> get() {
		return this.scenarios;
	}

	/**
	 * @return list of nested scenarios
	 */
	public Set<Scenario> list() {
		Set<Scenario> c = new SortedScenarioSet();
		for (Pair<Scenario, Double> s : this.scenarios)
			c.addAll(s.getKey().list());
		return c;
	}

	@SuppressWarnings("boxing")
	private static String list(Iterable<Pair<Scenario, Double>> list, boolean includeZeroWeight) {
		try {
			String qualifiedName = list.iterator().next().getKey().getClass().getName();
			String[] nameParts = qualifiedName.split("\\."); // this is just twice escaped dot symbol
			String scenarioClassName = nameParts[nameParts.length - 1]; // last one
			String packagePart = qualifiedName.replace(scenarioClassName, "");
			// Creating the set of Strings representing nested scenarios
			Set<String> c = new TreeSet<>();
			list.forEach(s -> {
				if (s.getValue() > 0 || includeZeroWeight)
					c.add(s.getKey().getClass().getName().toString().replace(packagePart, ""));
			});
			return packagePart + c.toString();
		} catch (@SuppressWarnings("unused") NoSuchElementException e) {
			return "Ø";
		}
	}

	/**
	 * Calculate upstream weight of all nested scenarios
	 * 
	 * @return calculated upstream weight
	 */
	@SuppressWarnings("boxing")
	public Double upstream() {
		double p = 0.0;
		for (Pair<Scenario, Double> s : this.scenarios)
			// if (!(s.getValue()).isNaN()) {
			if (s.getKey().executable()) {
				p = p + s.getValue(); // weight of a scenario
				p = p + s.getKey().upstream(); // weight of underlying
			}
		return p;
	}
}
