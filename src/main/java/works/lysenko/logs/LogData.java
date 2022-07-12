package works.lysenko.logs;

import static works.lysenko.Constants.EMPTY;
import static works.lysenko.Constants.u0020;
import static works.lysenko.Constants.u2022;

/**
 * @author Sergii Checkbox
 */
public interface LogData {

	/**
	 * Render log data
	 *
	 * @param d depth of log (number of added spaces)
	 * @param l level of log (number of dots)
	 * @param s message
	 * @return rendered log message
	 */
	public static String renderLog(int d, int l, String s) {
		StringBuilder sb = new StringBuilder();
		sb.append(u0020.repeat(Math.abs(d))); // logical scenario level
		sb.append(u2022.repeat(Math.abs(l))); // logical message level
		sb.append(0 == l ? EMPTY : u0020); // optional space
		sb.append(s); // message
		return sb.toString();
	}

	// TODO: why not .toString();?
	/**
	 * @return text representation of Log Data
	 */
	public String render();

}
