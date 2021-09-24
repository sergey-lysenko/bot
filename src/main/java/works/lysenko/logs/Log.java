package works.lysenko.logs;

@SuppressWarnings("javadoc")
public class Log extends AbstractLogData {

	int depth;
	int level;

	public Log(int depth, int level, String text) {
		super();
		this.depth = depth;
		this.level = level;
		this.text = text;
	}

	public int depth() {
		return depth;
	}

	public int level() {
		return level;

	}

	public String render() {
		return LogData.renderLog(depth, level, text);
	}

	public String text() {
		return text;
	}

	public String toString() {
		return "Log [depth=" + depth + ", level=" + level + ", text=" + text + "]";
	}

}
