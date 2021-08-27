package works.lysenko;

import java.util.ArrayList;
import java.util.List;

public class Result {
	public double weight;
	public int executions;
	public List<Problem> problems = new ArrayList<Problem>();

	public String toString() {
		return "(" + weight + ") " + executions
				+ ((problems != null && problems.size() > 0)
						? " (" + problems.size() + ((problems.size() == 1) ? " problem)" : " problems)")
						: "");
	}
}
