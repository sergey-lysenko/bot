package works.lysenko.logs;

/**
 * @author Sergii Lysenko
 */
public class Warning extends AbstractProblemLogData {

	/**
	 * @param depth in the output log
	 * @param text describing the problem
	 */
	public Warning(int depth, String text) {
		super(depth, text);
		this.tag = "[WARNING]";
	}

	@Override
	public String toString() {
		return "Severe [depth=" + this.depth + ", tag=" + this.tag + ", text=" + this.text + "]";
	}
}
