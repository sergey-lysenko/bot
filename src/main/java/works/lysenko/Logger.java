package works.lysenko;

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
import works.lysenko.logs.LineFeed;
import works.lysenko.logs.Log;
import works.lysenko.logs.LogRecord;
import works.lysenko.logs.Notice;
import works.lysenko.logs.Severe;
import works.lysenko.logs.Warning;
import works.lysenko.utils.Color;

public class Logger {

	public Run r;
	public Set<String> logsToRead;
	public PriorityQueue<LogRecord> log;
	protected BufferedWriter logWriter;

	public Logger(Set<String> logsToRead, Run r) {
		super();
		this.r = r;
		try {
			new File(Constants.DEFAULT_RUNS_LOCATION).mkdirs();
			logWriter = new BufferedWriter(new FileWriter(Constants.DEFAULT_RUNS_LOCATION
					+ Common.fill(Constants.RUN_LOG_FILENAME, String.valueOf(r.timer.startedAt()))));
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
	 * @param r
	 */
	public void log(int l, String s, Long t) {
		LogEntries ls;
		LogRecord lr;
		long time = (null == t) ? r.timer() : t;
		int depth = (null == r.current) ? 0 : r.current.depth();
		// TODO: rework that to cycle over LogType
		if (logsToRead.contains(LogType.BROWSER)) {
			ls = r.d.manage().logs().get(LogType.BROWSER);
			for (LogEntry e : ls) {
				log.add(problem(time, depth, "[BROWSER] " + e.toString()));
			}
		}
		if (logsToRead.contains(LogType.CLIENT)) {
			ls = r.d.manage().logs().get(LogType.CLIENT);
			for (LogEntry e : ls) {
				log.add(problem(time, depth, "[CLIENT] " + e.toString()));
			}
		}
		if (logsToRead.contains(LogType.DRIVER)) {
			ls = r.d.manage().logs().get(LogType.DRIVER);
			for (LogEntry e : ls) {
				log.add(problem(time, depth, "[DRIVER] " + e.toString()));
			}
		}
		// TODO: figure out unavailability of these three types of logs
		// (run.logs.contains(LogType.PERFORMANCE))
		// if (run.logs.contains(LogType.PROFILER))
		// if (run.logs.contains(LogType.SERVER))
		AbstractLogData ld = new Log((null == r.current) ? 0 : r.current.depth(), l, s);
		lr = new LogRecord(((null == t) ? r.timer() : t), ld);
		log.add(lr);
		process();
	}

	public void logProblem(String s) {
		int depth = (null == r.current) ? 0 : r.current.depth();
		log.add(problem(r.timer(), depth, s));
	}

	private static void logConsole(String s) {
		System.out.println(s);
	}

	private void logFile(String s) {
		try {
			if (null == logWriter)
				log(0, Color.colorize("[SEVERE] Log Writer is not initialised, unable to write log"));
			else
				logWriter.write(s + System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public void logln() {
		LogRecord lr;
		lr = new LogRecord(r.timer(), new LineFeed());
		log.add(lr);
	}

	private LogRecord problem(long t, int d, String p) {
		LogRecord lr;
		// TODO: rework that to cycle over Severity
		if (p.contains("[SEVERE]")) {
			lr = new LogRecord(t, new Severe(d, p.replaceAll("\\s+\\[SEVERE]\\s+", " ")));
		} else if (p.contains("[WARNING]")) {
			lr = new LogRecord(t, new Warning(d, p.replaceAll("\\s+\\[WARNING\\]\\s+", " ")));
		} else if (p.contains("[NOTICE]")) {
			lr = new LogRecord(t, new Notice(d, p.replaceAll("\\s+\\[NOTICE\\]\\s+", " ")));
		} else {
			lr = new LogRecord(t, new Notice(d, p));
		}
		r.problem(lr);
		return lr;
	}

	private void process() {
		while (!log.isEmpty()) {
			String line = log.poll().render();
			logConsole(line);
			logFile(line);
		}

	}
}
