package works.lysenko;

import static works.lysenko.Constants.RUNS;
import static works.lysenko.Constants.FILENAME;
import static works.lysenko.Constants.RUN_LOG_FILENAME;
import static works.lysenko.enums.Platform.CHROME;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import works.lysenko.enums.Ansi;
import works.lysenko.enums.Severity;
import works.lysenko.logs.AbstractLogData;
import works.lysenko.logs.KnownIssue;
import works.lysenko.logs.LineFeed;
import works.lysenko.logs.Log;
import works.lysenko.logs.LogRecord;
import works.lysenko.logs.Notice;
import works.lysenko.logs.Severe;
import works.lysenko.logs.Warning;

/**
 * @author Sergii Lysenko
 */
public class Logger {

	private static void logConsole(String s) {
		System.out.println(s);
	}

	/**
	 * Link to Execution object
	 */
	public Execution x;
	/**
	 * Set of browser logs to read
	 */
	public Set<String> logsToRead;
	/**
	 * Queue of log messages
	 */
	public PriorityQueue<LogRecord> log;
	protected BufferedWriter logWriter;
	private long prev = 0;

	private int length = 5;

	/**
	 * @param x
	 * @param logsToRead
	 */
	public Logger(Execution x, Set<String> logsToRead) {
		super();
		this.x = x;
		try {
			logWriter = new BufferedWriter(new FileWriter(Output.name(x, RUN_LOG_FILENAME)));
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

		if (x.in(CHROME)) {
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

	/**
	 * Write a string to a named log file
	 * 
	 * @param s  text to write
	 * @param n  name of log file
	 * @param ex extension of log file
	 */
	public void logFile(String s, String n, String ex) {
		String location = RUNS + x.parameters.get("TEST") + "/" + x.t.startedAt() + "/";
		String timestamp = String.format("%013d", x.t.millis());
		String name = Common.fill(FILENAME, timestamp, n, ex);
		String filename = location + name;

		BufferedWriter writer = null;
		try {
			new File(location).mkdirs();
			writer = new BufferedWriter(new FileWriter(filename));
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
			e.printStackTrace();
		}
		// Path target = Paths.get(".", name);
		Path link = Paths.get(location, Common.fill(FILENAME, "latest", n, ex));
		try {
			// if (Files.exists(link))
			Files.delete(link);
		} catch (IOException e1) {
			// NOP
		}
		/*try {
			Files.createSymbolicLink(link, target);
			TODO: fix for windows   
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * Log a problem as Known Issue
	 * 
	 * @param s description of known issue
	 */
	public void logKnownIssue(String s) {
		logProblem(Severity.SK, s);
	}

	/**
	 * Log an empty line
	 */
	public void logln() {
		LogRecord lr;
		lr = new LogRecord(x.timer(), new LineFeed());
		log.add(lr);
	}

	/**
	 * Log a problem
	 * 
	 * @param se severity of the problem
	 * @param st description of the problem
	 */
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
			x.newIssues.add(p);
		} else if (p.contains("[WARNING]")) {
			lr = new LogRecord(t, new Warning(d, p));
			x.newIssues.add(p);
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
			if (x._debug()) {
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