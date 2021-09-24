package works.lysenko.logs;

@SuppressWarnings("javadoc")
public class KnownIssue extends AbstractProblemLogData {

	public KnownIssue(int depth, String text) {
		super(depth, text);
		this.tag = "[KNOWN-ISSUE]";
	}

	public String toString() {
		return "Known Issue [depth=" + depth + ", tag=" + tag + ", text=" + text + "]";
	}

}
