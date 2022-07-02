package works.lysenko.logs;

/**
 * @author Sergii Lysenko
 */
public abstract class AbstractLogData implements LogData {

	String text;

	/**
	 * @return value of text field
	 */
	public String text() {
		return this.text;
	}
}
