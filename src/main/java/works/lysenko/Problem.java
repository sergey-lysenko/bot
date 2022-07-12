package works.lysenko;

/**
 * @author Sergii Lysenko
 *
 */
public class Problem {
	/**
	 * Timestamp of the problem occurence
	 */
	public long time;

	/**
	 * Description of the problem
	 */
	public String text;

	/**
	 * @param m time
	 * @param s description
	 */
	public Problem(long m, String s) {
		super();
		this.time = m;
		this.text = s;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return "(" + this.time + ") " + this.text;
	}
}
