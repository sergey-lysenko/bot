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
		return depth;
	}

	/**
	 * @return level
	 */
	public int level() {
		return level;
	}

	@Override
	public String render() {
		return LogData.renderLog(depth, level, text);
	}

	@Override
	public String text() {
		return text;
	}

	@Override
	public String toString() {
		return "Log [depth=" + depth + ", level=" + level + ", text=" + text + "]";
	}

}
