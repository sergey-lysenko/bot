package works.lysenko.logs;

@SuppressWarnings("javadoc")
public class Warning extends AbstractProblemLogData {

	public Warning(int depth, String text) {
		super(depth, text);
		this.tag = "[WARNING]";
	}

	public String toString() {
		return "Severe [depth=" + depth + ", tag=" + tag + ", text=" + text + "]";
	}
}
