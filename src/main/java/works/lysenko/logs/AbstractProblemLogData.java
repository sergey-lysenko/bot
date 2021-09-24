package works.lysenko.logs;

import works.lysenko.utils.Ansi;
import works.lysenko.utils.Severity;

/**
 * @author Sergii Lysenko
 */
public abstract class AbstractProblemLogData extends AbstractLogData {

	int depth;
	String tag;

	/**
	 * @param depth
	 * @param text
	 */
	public AbstractProblemLogData(int depth, String text) {
		super();
		this.depth = depth;
		this.text = text;
	}

	/**
	 * @return default text representation of abstract Log Data
	 */
	public String render() {
		return LogData.renderLog(depth, 0, Ansi.colorize(text, Severity.byCode(tag).color()));
	}

}
