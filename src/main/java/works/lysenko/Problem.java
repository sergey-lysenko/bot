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
		time = m;
		text = s;
	}

	@Override
	public String toString() {
		return "(" + time + ") " + text;
	}
}
