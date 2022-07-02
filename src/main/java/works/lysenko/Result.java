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
	public Double cWeight;

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
		this.type = s.type();
		this.cWeight = s.weight();
		this.uWeight = s.upstream();
		this.dWeight = s.downstream();
	}

	@Override
	public String toString() {
		String weight = (this.dWeight > 0.0 ? "(" + this.dWeight + ")" + "\u2192" : "") + "(" + this.cWeight + ")"
				+ (this.uWeight > 0.0 ? "\u2190" + "(" + this.uWeight + ")" : "");
		String strProblems = this.problems != null && this.problems.size() > 0
				? " (" + this.problems.size() + (this.problems.size() == 1 ? " problem)" : " problems)")
				: "";
		return weight + " " + this.executions + strProblems;
	}
}
