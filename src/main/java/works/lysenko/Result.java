package works.lysenko;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
public class Result {
	public ScenarioType type = null;
	public int executions;
	public List<Problem> problems = new ArrayList<Problem>();
	public double confWeight;
	public double pervWeight;
	public double permWeight;

	public String toString() {
		String weight = ((permWeight > 0.0) ? ("(" + permWeight + ")" + "\u2192") : "") + "(" + confWeight + ")"
				+ ((pervWeight > 0.0) ? "\u2190" + "(" + pervWeight + ")" : "");
		String problems = ((this.problems != null && this.problems.size() > 0)
				? " (" + this.problems.size() + ((this.problems.size() == 1) ? " problem)" : " problems)")
				: "");
		return weight + " " + executions + problems;
	}
}
