package works.lysenko.logs;

/**
 * @author Sergii Lysenko
 */
public class Log extends AbstractLogData {

	int depth;
	int level;

	/**
	 * @param depth in the output log
	 * @param level 
	 * @param text describing the problem
	 */
	public Log(int depth, int level, String text) {
		super();
		this.depth = depth;
		this.level = level;
		this.text = text;
	}

	/**
	 * @return depth in the output log
	 */
	public int depth() {
		return this.depth;
	}

	/**
	 * @return level
	 */
	public int level() {
		return this.level;
	}

	@Override
	public String render() {
		return LogData.renderLog(this.depth, this.level, this.text);
	}

	@Override
	public String text() {
		return this.text;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return "Log [depth=" + this.depth + ", level=" + this.level + ", text=" + this.text + "]";
	}

}
