package works.lysenko;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import works.lysenko.logs.LogRecord;
import works.lysenko.utils.SortedResultMap;

/**
 * This class represent results of single bot execution
 * 
 * @author Sergii Lysenko
 *
 */
public class Results {

	public Execution x;
	private Map<String, Result> results;

	public Results(Execution x) {
		super();
		this.x = x;
		results = new HashMap<String, Result>();
	}

	/**
	 * Count in test execution (in later versions there will be collection of more
	 * execution data then just times of execution)
	 * 
	 * @param s          tag of test execution
	 * @param confWeight configuration eight coefficient
	 * @param pervWeight
	 * @param permWeight
	 * @return copy of added test execution data
	 */
	public Result countScenario(String s, ScenarioType t, double confWeight, double pervWeight, double permWeight) {
		Result r = results.getOrDefault(s, new Result());
		{
			r.confWeight = confWeight;
			r.pervWeight = pervWeight;
			r.permWeight = permWeight;
			++r.executions;
			r.type = t;
		}
		results.put(s, r);
		return r;
	}

	/**
	 * This routine stores the information about a problem in the execution results
	 */
	protected void problem(LogRecord lr) {
		if (null != x.currentScenario())
			results.get(x.currentScenario().shortName(true)).problems.add(new Problem(lr.time(), lr.text()));
	}

	/**
	 * Sort scenario classes ignoring the case which produces more "tree-like" order
	 * of items and improves readability, excluding not executed scenarios
	 * 
	 * @return execution counters sorted properly
	 */
	protected TreeMap<String, Result> getSorted() {
		TreeMap<String, Result> sorted = new SortedResultMap();
		sorted.putAll(this.results);
		return sorted;
	}

	/**
	 * Sort scenario classes ignoring the case which produces more "tree-like" order
	 * of items and improves readability, including or excluding not executed
	 * scenarios
	 * 
	 * @param b whether to include non-executed scenarios or not
	 * @return execution counters sorted properly
	 */
	public TreeMap<String, Result> getSorted(boolean ignoreCase, boolean shortened) {
		TreeMap<String, Result> sorted;
		if (ignoreCase)
			sorted = new SortedResultMap();
		else
			sorted = new TreeMap<String, Result>();
		sorted.putAll(this.results);
		for (String s : x.cycles.scenariosList(shortened, true)) {
			if (!sorted.containsKey(s))
				sorted.put(s, new Result());
		}
		return sorted;
	}

}
