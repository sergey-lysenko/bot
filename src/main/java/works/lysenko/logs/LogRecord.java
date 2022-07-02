package works.lysenko.logs;

import java.text.DecimalFormat;

/**
 * @author Sergii Lysenko
 */
public class LogRecord {

	/**
	 * @param t time
	 * @return formatted time
	 */
	@SuppressWarnings("boxing")
	public static String renderTime(Long t) {
		return "[" + new DecimalFormat("0000000.000").format(Double.valueOf(t) / 1000) + "]";
	}

	long time;

	AbstractLogData data;

	/**
	 * @param time
	 * @param data
	 */
	public LogRecord(long time, AbstractLogData data) {
		super();
		this.data = data;
		this.time = time;
	}

	/**
	 * @param time
	 * @param data
	 */
	public LogRecord(long time, KnownIssue data) {
		super();
		this.data = data;
		this.time = time;
	}

	/**
	 * @param time
	 * @param data
	 */
	public LogRecord(long time, Notice data) {
		super();
		this.data = data;
		this.time = time;
	}

	/**
	 * @param time
	 * @param data
	 */
	public LogRecord(long time, Severe data) {
		super();
		this.data = data;
		this.time = time;
	}

	/**
	 * @param time
	 * @param data
	 */
	public LogRecord(long time, Warning data) {
		super();
		this.data = data;
		this.time = time;
	}

	/**
	 * @return data
	 */
	public LogData data() {
		return this.data;
	}

	/**
	 * @return rendered LogRecord
	 */
	@SuppressWarnings("boxing")
	public String render() {
		return renderTime(this.time) + " " + this.data.render();
	}

	/**
	 * @return text of the LogRecord
	 */
	public String text() {
		return this.data.text();
	}

	/**
	 * @return time of the LogRecord
	 */
	@SuppressWarnings("boxing")
	public Long time() {
		return this.time;
	}

	@Override
	public String toString() {
		return "LogRecord [data=" + this.data + "time=" + this.time + "]";
	}
}
