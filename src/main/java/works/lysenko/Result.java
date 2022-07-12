package works.lysenko;

import static works.lysenko.Constants.EMPTY;
import static works.lysenko.Constants._PROBLEM;
import static works.lysenko.Constants.u0020;
import static works.lysenko.Constants.u0028;
import static works.lysenko.Constants.u0029;
import static works.lysenko.Constants.u0073;
import static works.lysenko.Constants.u2190;
import static works.lysenko.Constants.u2192;

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
		String weight = (this.dWeight > 0.0 ? u0028 + this.dWeight + u0029 + u2192 : EMPTY) + u0028 + this.cWeight
				+ u0029 + (this.uWeight > 0.0 ? u2190 + u0028 + this.uWeight + u0029 : EMPTY);
		String strProblems = this.problems != null && this.problems.size() > 0
				? u0020 + u0028 + this.problems.size() + _PROBLEM + (this.problems.size() == 1 ? EMPTY : u0073) + u0029
				: EMPTY;
		return weight + u0020 + this.executions + strProblems;
	}
}
