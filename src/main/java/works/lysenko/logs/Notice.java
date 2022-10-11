package works.lysenko.logs;

/**
 * @author Sergii Lysenko
 */
public class Notice extends AbstractProblemLogData {

	/**
	 * @param depth in the output log
	 * @param text describing the problem
	 */
	public Notice(int depth, String text) {
		super(depth, text);
		tag = "[NOTICE]";
	}

	@Override
	public String toString() {
		return "Notice [depth=" + depth + ", tag=" + tag + ", text=" + text + "]";
	}

}
