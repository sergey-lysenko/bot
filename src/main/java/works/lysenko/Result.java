package works.lysenko;

import java.util.ArrayList;
import java.util.List;

import works.lysenko.enums.ScenarioType;
import works.lysenko.scenarios.Scenario;

/**
 * Result record
 * 
 * @author Sergii Lysenko
 */
public class Result {

	/**
	 * type of the Scenario
	 */
	public ScenarioType type = null;

	/**
	 * Number of Scenario executions
	 */
	public int executions;

	/**
	 * 
	 */
	public List<Problem> problems = new ArrayList<>();

	/**
	 * Weight of the Scenario defined in configuration
	 */
	public double cWeight;

	/**
	 * Upstream weight of the Scenario
	 */
	public double uWeight;

	/**
	 * Downstream weight of the Scenario
	 */
	public double dWeight;

	/**
	 * @param s Scenario for this Result
	 */
	public Result(Scenario s) {
		type = s.type();
		cWeight = s.weight();
		uWeight = s.upstream();
		dWeight = s.downstream();
	}

	@Override
	public String toString() {
		String weight = (dWeight > 0.0 ? "(" + dWeight + ")" + "\u2192" : "") + "(" + cWeight + ")"
				+ (uWeight > 0.0 ? "\u2190" + "(" + uWeight + ")" : "");
		String problems = this.problems != null && this.problems.size() > 0
				? " (" + this.problems.size() + (this.problems.size() == 1 ? " problem)" : " problems)")
				: "";
		return weight + " " + executions + problems;
	}
}
