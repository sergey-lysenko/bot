package works.lysenko;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import works.lysenko.logs.LogRecord;
import works.lysenko.scenarios.Scenario;
import works.lysenko.utils.SortedResultMap;

/**
 * This class represent results of single bot execution
 *
 * @author Sergii Lysenko
 *
 */
public class Results {

	/**
	 *
	 */
	public Execution x;
	private Map<Scenario, Result> results;

	/**
	 * @param x
	 */
	public Results(Execution x) {
		super();
		this.x = x;
		this.results = new HashMap<>();
	}

	/**
	 * Count in test execution (in later versions there will be collection of more
	 * execution data then just times of execution)
	 *
	 * @param s scenario to count
	 * @return copy of added test execution data
	 */
	public Result count(Scenario s) {
		Result r = this.results.getOrDefault(s, new Result(s));
		++r.executions;
		this.results.put(s, r);
		return r;
	}

	/**
	 * Sort scenario classes ignoring the case which produces more "tree-like" order
	 * of items and improves readability, excluding not executed scenarios
	 *
	 * @return execution counters sorted properly
	 */
	protected TreeMap<Scenario, Result> getSorted() {
		TreeMap<Scenario, Result> sorted = new SortedResultMap();
		sorted.putAll(this.results);
		return sorted;
	}

	/**
	 * Sort scenario classes ignoring the case which produces more "tree-like" order
	 * of items and improves readability, including not executed scenarios
	 *
	 * @param ignoreCase
	 * @param shortened
	 * @return execution counters sorted properly
	 */
	public TreeMap<String, Result> getSortedStrings() {
		TreeMap<String, Result> sorted = new TreeMap<>();
		// Adding results of executed scenarios
		for (Entry<Scenario, Result> r : this.results.entrySet())
			sorted.put(tagged(r.getKey()), r.getValue());
		// Adding result stubs of non-executed scenarios
		for (Scenario s : this.x.cycles.scenariosList())
			if (!sorted.containsKey(tagged(s)))
				sorted.put(tagged(s), new Result(s));
		return sorted;
	}

	/**
	 * This routine stores the information about a problem in the execution results
	 */
	@SuppressWarnings("boxing")
	protected void problem(LogRecord lr) {
		if (null != this.x.currentScenario())
			this.results.get(this.x.currentScenario()).problems.add(new Problem(lr.time(), lr.text()));
	}

	private static String tagged(Scenario s) {
		return s.shortName() + " " + s.type().tag();
	}
}
