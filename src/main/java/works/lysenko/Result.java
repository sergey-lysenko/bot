package works.lysenko;

import java.util.ArrayList;
import java.util.List;

import works.lysenko.enums.ScenarioType;
import works.lysenko.scenarios.Scenario;

@SuppressWarnings("javadoc")
public class Result {

	public ScenarioType type = null;
	public int executions;
	public List<Problem> problems = new ArrayList<Problem>();
	public double cWeight;
	public double uWeight;
	public double dWeight;

	public Result(Scenario s) {
		type = s.type();
		cWeight = s.weight();
		uWeight = s.upstream();
		dWeight = s.downstream();
	}

	public String toString() {
		String weight = ((dWeight > 0.0) ? ("(" + dWeight + ")" + "\u2192") : "") + "(" + cWeight + ")"
				+ ((uWeight > 0.0) ? "\u2190" + "(" + uWeight + ")" : "");
		String problems = ((this.problems != null && this.problems.size() > 0)
				? " (" + this.problems.size() + ((this.problems.size() == 1) ? " problem)" : " problems)")
				: "");
		return weight + " " + executions + problems;
	}
}
