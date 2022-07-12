package works.lysenko.logs;

/**
 * @author Sergii Lysenko
 */
public class Severe extends AbstractProblemLogData {

	/**
	 * @param depth in the output log
	 * @param text describing the problem
	 */
    public Severe(int depth, String text) {
		super(depth, text);
		this.tag = "[SEVERE]"; //$NON-NLS-1$
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return "Severe [depth=" + this.depth + ", tag=" + this.tag + ", text=" + this.text + "]";
	}

}
