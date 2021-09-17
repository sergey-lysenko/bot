package works.lysenko;

import static works.lysenko.Constants.DEFAULT_RUNS_LOCATION;
import static works.lysenko.Constants.FILENAME;
import static works.lysenko.Constants.RUN_LOG_FILENAME;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import works.lysenko.logs.AbstractLogData;
import works.lysenko.logs.KnownIssue;
import works.lysenko.logs.LineFeed;
import works.lysenko.logs.Log;
import works.lysenko.logs.LogRecord;
import works.lysenko.logs.Notice;
import works.lysenko.logs.Severe;
import works.lysenko.logs.Warning;
import works.lysenko.utils.Ansi;
import works.lysenko.utils.Severity;

public class Logger {

	private static void logConsole(String s) {
		System.out.println(s);
	}
	public Execution x;
	public Set<String> logsToRead;
	public PriorityQueue<LogRecord> log;
	protected BufferedWriter logWriter;
	private long prev = 0;

	private int length = 5;

	public Logger(Execution x, Set<String> logsToRead) {
		super();
		this.x = x;
		try {
			new File(DEFAULT_RUNS_LOCATION).mkdirs();
			logWriter = new BufferedWriter(new FileWriter(
					DEFAULT_RUNS_LOCATION + Common.fill(RUN_LOG_FILENAME, String.valueOf(x.t.startedAt()))));
		} catch (IOException e) {
			throw new RuntimeException("Unable to open log file for writting");
		}
		this.logsToRead = (null == logsToRead) ? new HashSet<String>() : logsToRead;
		this.log = new PriorityQueue<LogRecord>((new Comparator<LogRecord>() {
			public int compare(LogRecord o1, LogRecord o2) {
				return o1.time().compareTo(o2.time());
			}
		}));
	}

	/**
	 * Write a string of text to test execution log with defined logical level as
	 * visual queues, for example:
	 * 
	 * This is the level 0 record
	 * 
	 * • This is the level 1 record
	 * 
	 * •• This is the level 2 record
	 * 
	 * @param l log level, 0 is for no markers, 1-n is for number of '•' markers
	 *          before a string
	 * @param s string of text to be written to log
	 */
	public void log(int l, String s) {
		log(l, s, (Long) null);
	}

	/**
	 * Write a string of text to test execution log with preceding check of browser
	 * console for present errors and eventual output of found browser console logs
	 * 
	 * @param l log level, 0 is for no markers, 1-n is for number of '•' markers
	 *          before a string
	 * @param s string of text to be written to log
	 * @param t redefined time of event, or it can be set to null for measurement by
	 *          this method
	 * @param x
	 */
	public void log(int l, String s, Long t) {
		LogEntries ls;
		LogRecord lr;
		int depth = x.currentDepth();

		if (logsToRead.contains(LogType.BROWSER)) {
			ls = x.d.manage().logs().get(LogType.BROWSER);
			long time = ((null == t) ? x.timer() : t);
			for (LogEntry e : ls) {
				long timestamp = e.getTimestamp() - x.t.startedAt(); // since test start
				long difference = time - timestamp; // since capturing
				log.add(problem(timestamp, depth, "[" + difference + "]" + " [BROWSER] " + e.toString()));
			}
		}

		if (logsToRead.contains(LogType.CLIENT)) {
			ls = x.d.manage().logs().get(LogType.CLIENT);
			long time = ((null == t) ? x.timer() : t);
			for (LogEntry e : ls) {
				log.add(problem(time, depth, "[CLIENT] " + e.toString()));
			}
		}

		if (logsToRead.contains(LogType.DRIVER)) {
			ls = x.d.manage().logs().get(LogType.DRIVER);
			long time = ((null == t) ? x.timer() : t);
			for (LogEntry e : ls) {
				log.add(problem(time, depth, "[DRIVER] " + e.toString()));
			}
		}

		AbstractLogData ld = new Log(x.currentDepth(), l, s);
		long time = ((null == t) ? x.timer() : t);
		lr = new LogRecord(time, ld);
		log.add(lr);
		process();
	}

	/**
	 * Write a string of text into the test execution log with logical level 1, for
	 * example:
	 * 
	 * • This is the level 1 record
	 * 
	 * @param s string of text to be written to log
	 */
	public void log(String s) {
		log(1, s);
	}

	private void logFile(String s) {
		try {
			if (null == logWriter)
				log(0, Ansi.colorize("[SEVERE] Log Writer is not initialised, unable to write log"));
			else
				logWriter.write(s + System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logFile(String s, String n, String ex) {
		String location = DEFAULT_RUNS_LOCATION + x.t.startedAt() + "/";
		BufferedWriter writer = null;
		try {
			new File(location).mkdirs();
			writer = new BufferedWriter(
					new FileWriter(location + Common.fill(FILENAME, String.format("%013d", x.t.millis()), n, ex)));
		} catch (IOException e) {
			throw new RuntimeException("Unable to open a file for writting");
		}
		try {
			writer.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void logKnownIssue(String s) {
		logProblem(Severity.SK, s);
	}

	public void logln() {
		LogRecord lr;
		lr = new LogRecord(x.timer(), new LineFeed());
		log.add(lr);
	}

	public void logProblem(Severity se, String st) {
		int depth = x.currentDepth();
		log.add(problem(x.timer(), depth, se.tag() + " " + st));
	}

	private LogRecord problem(long t, int d, String p) {
		LogRecord lr;
		Set<String> ki = x.getKnownIssue(p);
		if (ki.size() > 0) {
			lr = new LogRecord(t, new KnownIssue(d, "[KNOWN-ISSUE]:" + ki.toString() + ":" + p));
		} else if (p.contains("[SEVERE]")) {
			lr = new LogRecord(t, new Severe(d, p));
		} else if (p.contains("[WARNING]")) {
			lr = new LogRecord(t, new Warning(d, p));
		} else if (p.contains("[NOTICE]")) {
			lr = new LogRecord(t, new Notice(d, p));
		} else if (p.contains("[KNOWN-ISSUE]")) {
			lr = new LogRecord(t, new KnownIssue(d, p));
		} else {
			lr = new LogRecord(t, new Notice(d, p));
		}

		x.r.problem(lr);
		return lr;
	}

	private void process() {
		while (!log.isEmpty()) {
			long since = 0;
			String debug = "";
			LogRecord r = log.poll();
			Long time = r.time();
			String line = r.render();
			if (x.debug()) {
				since = time - prev;
				int thisLength = String.valueOf(since).length();
				if (thisLength > length)
					length = thisLength;
				debug = "[" + String.format("%1$" + length + "s", time - prev) + "]";
			}
			logConsole(debug + line);
			logFile(debug + line);
			prev = time;
		}
	}
}
