package works.lysenko;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
public class Result {
	public ScenarioType type = null;
	public int executions;
	public List<Problem> problems = new ArrayList<Problem>();
	public double cWeight;
	public double uWeight;
	public double dWeight;

	public String toString() {
		String weight = ((dWeight > 0.0) ? ("(" + dWeight + ")" + "\u2192") : "") + "(" + cWeight + ")"
				+ ((uWeight > 0.0) ? "\u2190" + "(" + uWeight + ")" : "");
		String problems = ((this.problems != null && this.problems.size() > 0)
				? " (" + this.problems.size() + ((this.problems.size() == 1) ? " problem)" : " problems)")
				: "");
		return weight + " " + executions + problems;
	}
}
