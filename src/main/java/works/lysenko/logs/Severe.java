package works.lysenko.logs;

public class Severe extends AbstractProblemLogData {

	public Severe(int depth, String text) {
		super(depth, text);
		tag = "[SEVERE]";
	}

	public String toString() {
		return "Severe [depth=" + depth + ", tag=" + tag + ", text=" + text + "]";
	}

}
