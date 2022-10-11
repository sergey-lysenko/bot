package works.lysenko.logs;

/**
 * @author Sergii Lysenko
 */
public class KnownIssue extends AbstractProblemLogData {

	/**
	 * @param depth in the output log
	 * @param text  describing the problem
	 */
	public KnownIssue(int depth, String text) {
		super(depth, text);
		tag = "[KNOWN-ISSUE]";
	}

	@Override
	public String toString() {
		return "Known Issue [depth=" + depth + ", tag=" + tag + ", text=" + text + "]";
	}

}
