package works.lysenko.logs;

import java.text.DecimalFormat;

public class LogRecord {

	long time;
	AbstractLogData data;

	public LogRecord(long time, AbstractLogData data) {
		super();
		this.data = data;
		this.time = time;
	}

	public LogRecord(long time, Notice data) {
		super();
		this.data = data;
		this.time = time;
	}

	public LogRecord(long time, Severe data) {
		super();
		this.data = data;
		this.time = time;
	}

	public LogRecord(long time, Warning data) {
		super();
		this.data = data;
		this.time = time;
	}

	public LogRecord(long time, KnownIssue data) {
		super();
		this.data = data;
		this.time = time;
	}

	public String text() {
		return data.text();
	}

	public Long time() {
		return time;
	}

	public LogData data() {
		return data;
	}

	public String render() {
		return renderTime(time) + " " + data.render();
	}

	public String toString() {
		return "LogRecord [data=" + data + "time=" + time + "]";
	}

	public static String renderTime(Long t) {
		return "[" + new DecimalFormat("0000000.000").format(Double.valueOf(t) / 1000) + "]";
	}
}
